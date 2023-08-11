#include <jni.h>
#include <string>
#include <fluidsynth.h>
#include <unistd.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_x8192Bit_DIYEdit_Mobile_MIDIFragment_PlayMIDFile(const char *path) {
    // Setup synthesizer
    fluid_settings_t *settings = new_fluid_settings();
    fluid_synth_t *synth = new_fluid_synth(settings);
    fluid_audio_driver_t *adriver = new_fluid_audio_driver(settings, synth);

    // Load sample soundfont
    fluid_synth_sfload(synth, path, 1);

    fluid_player_add()

    // Clean up
    delete_fluid_audio_driver(adriver);
    delete_fluid_synth(synth);
    delete_fluid_settings(settings);

}
