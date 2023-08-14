cmake_minimum_required(VERSION 3.8.1)

# project(midi-interface)

set(fluidsynth_DIR ${CMAKE_CURRENT_LIST_DIR}/fluidsynth)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -static-openmp -fopenmp -Lopenmp")
set(CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} -static-openmp -fopenmp -Lopenmp")

#region libFluidSynth import

add_library(libFLAC SHARED IMPORTED)
set_target_properties(libFLAC PROPERTIES IMPORTED_LOCATION ${fluidsynth_DIR}/lib/${ANDROID_ABI}/libFLAC.so)

add_library(libfluidsynth SHARED IMPORTED)
set_target_properties(libfluidsynth PROPERTIES IMPORTED_LOCATION ${fluidsynth_DIR}/lib/${ANDROID_ABI}/libfluidsynth.so)

add_library(libfluidsynth-assetloader SHARED IMPORTED)
set_target_properties(libfluidsynth-assetloader PROPERTIES IMPORTED_LOCATION ${fluidsynth_DIR}/lib/${ANDROID_ABI}/libfluidsynth-assetloader.so)

add_library(libgio-2.0 SHARED IMPORTED)
set_target_properties(libgio-2.0 PROPERTIES IMPORTED_LOCATION ${fluidsynth_DIR}/lib/${ANDROID_ABI}/libgio-2.0.so)

add_library(libglib-2.0 SHARED IMPORTED)
set_target_properties(libglib-2.0 PROPERTIES IMPORTED_LOCATION ${fluidsynth_DIR}/lib/${ANDROID_ABI}/libglib-2.0.so)

add_library(libgmodule-2.0 SHARED IMPORTED)
set_target_properties(libgmodule-2.0 PROPERTIES IMPORTED_LOCATION ${fluidsynth_DIR}/lib/${ANDROID_ABI}/libgmodule-2.0.so)

add_library(libgobject-2.0 SHARED IMPORTED)
set_target_properties(libgobject-2.0 PROPERTIES IMPORTED_LOCATION ${fluidsynth_DIR}/lib/${ANDROID_ABI}/libgobject-2.0.so)

add_library(libgthread-2.0 SHARED IMPORTED)
set_target_properties(libgthread-2.0 PROPERTIES IMPORTED_LOCATION ${fluidsynth_DIR}/lib/${ANDROID_ABI}/libgthread-2.0.so)

add_library(liboboe SHARED IMPORTED)
set_target_properties(liboboe PROPERTIES IMPORTED_LOCATION ${fluidsynth_DIR}/lib/${ANDROID_ABI}/liboboe.so)

add_library(libogg SHARED IMPORTED)
set_target_properties(libogg PROPERTIES IMPORTED_LOCATION ${fluidsynth_DIR}/lib/${ANDROID_ABI}/libogg.so)

add_library(libsndfile SHARED IMPORTED)
set_target_properties(libsndfile PROPERTIES IMPORTED_LOCATION ${fluidsynth_DIR}/lib/${ANDROID_ABI}/libsndfile.so)

add_library(libvorbis SHARED IMPORTED)
set_target_properties(libvorbis PROPERTIES IMPORTED_LOCATION ${fluidsynth_DIR}/lib/${ANDROID_ABI}/libvorbis.so)

add_library(libvorbisenc SHARED IMPORTED)
set_target_properties(libvorbisenc PROPERTIES IMPORTED_LOCATION ${fluidsynth_DIR}/lib/${ANDROID_ABI}/libvorbisenc.so)

add_library(libvorbisfile SHARED IMPORTED)
set_target_properties(libvorbisfile PROPERTIES IMPORTED_LOCATION ${fluidsynth_DIR}/lib/${ANDROID_ABI}/libvorbisfile.so)

#endregion

add_library(libomp SHARED IMPORTED)
set_target_properties(libvorbisfile PROPERTIES IMPORTED_LOCATION ${fluidsynth_DIR}/lib/${ANDROID_ABI}/libomp.so)

# Library that will be called directly from JAVA
add_library(midi-interface SHARED midi-interface.cpp)

# Specifies the directory where the C or C++ source code will look the #include <yourlibrary.h> header files
target_include_directories(midi-interface PRIVATE ${fluidsynth_DIR}/include)

# Link everything all together. Notice that midi-interface should be the first element in the list.
target_link_libraries(
        midi-interface
        libFLAC
        libfluidsynth
        libfluidsynth-assetloader
        libgio-2.0
        libglib-2.0
        libgmodule-2.0
        libgobject-2.0
        libgthread-2.0
        liboboe
        libogg
        libsndfile
        libvorbis
        libvorbisenc
        libvorbisfile
)