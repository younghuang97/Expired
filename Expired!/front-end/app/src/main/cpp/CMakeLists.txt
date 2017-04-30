cmake_minimum_required(VERSION 3.4.1)
add_library( # Specifies the name of the library.
             Fridge

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/Fridge.cpp )
add_library( # Specifies the name of the library.
             Item

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/Item.cpp )
include_directories(src/main/cpp/include/)