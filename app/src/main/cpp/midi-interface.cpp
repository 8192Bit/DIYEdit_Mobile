#include <jni.h>
#include <unistd.h>
#include <fluidsynth.h>
#include <cstdlib>
#include <iostream>
#include <cstring>
#include <android/log.h>

extern "C" {

JNIEXPORT void JNICALL
Java_com_x8192Bit_DIYEdit_1Mobile_Fragments_PlayThread_PlayMIDIFile(JNIEnv *env, jobject,
                                                                    jstring MIDIFilePath,
                                                                    jstring SoundFontFilePath) {
    fluid_settings_t *settings;
    fluid_synth_t *synth;
    fluid_player_t *player;
    fluid_audio_driver_t *aDriver;

    settings = new_fluid_settings();
    synth = new_fluid_synth(settings);
    player = new_fluid_player(synth);
    aDriver = new_fluid_audio_driver(settings, synth);
    fluid_synth_sfload(synth, env->GetStringUTFChars(SoundFontFilePath, nullptr), 1);
    fluid_player_add(player, env->GetStringUTFChars(MIDIFilePath, nullptr));
    // PROBLEM HERE
    __android_log_print(1, "MIDI DEBUG", "ERROR HERE");
    fluid_player_play(player);
    fluid_player_join(player);
    delete_fluid_audio_driver(aDriver);
    delete_fluid_player(player);
    delete_fluid_synth(synth);
    delete_fluid_settings(settings);
}


}