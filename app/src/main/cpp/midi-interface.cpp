#include <jni.h>
#include <unistd.h>
#include <fluidsynth.h>

fluid_synth_t *synth;
fluid_audio_driver_t *aDriver;
fluid_sequencer_t *sequencer;
short synthSeqID, mySeqID;
unsigned int now;
unsigned int seqDuration;

extern "C" {

//#region prototype
void seq_callback(unsigned int time, fluid_event_t *event, fluid_sequencer_t *seq, void *data);

JNIEXPORT jstring JNICALL
Java_com_x8192Bit_DIYEdit_1Mobile_MIDIFragment_PlayMIDIFile(JNIEnv *env, jobject, jstring);

void createSynth();

void deleteSynth();

void loadSoundFont(jstring filename);

void sendNoteOn(int chan, short key, unsigned int date);

void schedule_next_sequence();
//#endregion

JNIEXPORT jstring JNICALL
Java_com_x8192Bit_DIYEdit_1Mobile_Fragments_MIDIFragment_PlayMIDIFile(JNIEnv *env, jobject,
                                                                      jstring MIDIFilePath,
                                                                      jstring SoundFontFilePath) {

    createSynth();
    loadSoundFont(SoundFontFilePath);

    now = fluid_sequencer_get_tick(sequencer);
    schedule_next_sequence();

    deleteSynth();
    return (jstring) "114514";
}

/*TODO IMPLEMENT IT LATER
JNIEXPORT jstring JNICALL
Java_com_x8192Bit_DIYEdit_1Mobile_Fragments_MIDIFragment_GetAssetSfLoader(JNIEnv *env, jobject
                                                                      ) {

    createSynth();
    loadSoundFont(nullptr);

    now = fluid_sequencer_get_tick(sequencer);
    schedule_next_sequence();

    sleep(100000);
    deleteSynth();
    return (jstring) "114514";
}
*/

void createSynth() {
    fluid_settings_t *settings;
    settings = new_fluid_settings();
    fluid_settings_setint(settings, "synth.reverb.active", 1);
    fluid_settings_setint(settings, "synth.chorus.active", 1);
    synth = new_fluid_synth(settings);
    aDriver = new_fluid_audio_driver(settings, synth);
    sequencer = new_fluid_sequencer2(0);

    // register synth as first destination
    synthSeqID = fluid_sequencer_register_fluidsynth(sequencer, synth);

    // register myself as second destination
    mySeqID = fluid_sequencer_register_client(sequencer, "me", seq_callback, nullptr);

    // the sequence duration, in ms
    seqDuration = 1000;
}

void deleteSynth() {
    delete_fluid_sequencer(sequencer);
    delete_fluid_audio_driver(aDriver);
    delete_fluid_synth(synth);
}

void loadSoundFont(jstring filename) {
    int fluid_res;
    fluid_res = fluid_synth_sfload(synth, reinterpret_cast<const char *>(filename), 1);
    printf("%d", fluid_res);
}

void sendNoteOn(int chan, short key, unsigned int date) {
    int fluid_res;
    fluid_event_t *evt = new_fluid_event();
    fluid_event_set_source(evt, -1);
    fluid_event_set_dest(evt, synthSeqID);
    fluid_event_noteon(evt, chan, key, 127);
    fluid_res = fluid_sequencer_send_at(sequencer, evt, date, 1);
    delete_fluid_event(evt);
}

void schedule_next_callback() {
    int fluid_res;
    // I want to be called back before the end of the next sequence
    unsigned int callbackdate = now + seqDuration / 2;
    fluid_event_t *evt = new_fluid_event();
    fluid_event_set_source(evt, -1);
    fluid_event_set_dest(evt, mySeqID);
    fluid_event_timer(evt, nullptr);
    fluid_res = fluid_sequencer_send_at(sequencer, evt, callbackdate, 1);
    delete_fluid_event(evt);
}

void schedule_next_sequence() {
    // Called more or less before each sequence start
    // the next sequence start date
    now = now + seqDuration;

    // the sequence to play

    // the beat : 2 beats per sequence
    fluid_synth_noteon(synth, 0, 60, 127); // play middle C
    sleep(1); // sleep for 1 second
    fluid_synth_noteoff(synth, 0, 60); // stop playing middle C
    fluid_synth_noteon(synth, 0, 62, 127);
    sleep(1);
    fluid_synth_noteoff(synth, 0, 62);
    fluid_synth_noteon(synth, 0, 64, 127);
    sleep(1);
    fluid_synth_noteoff(synth, 0, 64);
    // so that we are called back early enough to schedule the next sequence
    schedule_next_callback();
}

/* sequencer callback */

void seq_callback(unsigned int time, fluid_event_t *event, fluid_sequencer_t *seq, void *data) {
    schedule_next_sequence();
}

}