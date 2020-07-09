# CMakeLists.txt for C++ Native Client App
cmake_minimum_required(VERSION 3.10)

project(Client1 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 11)

set(CMAKE_MODULE_PATH ${PROJECT_SOURCE_DIR}/cmake)

find_package(GeodeNative REQUIRED COMPONENTS cpp)

add_executable(Client1 cl.cpp Order.cpp)

target_link_libraries(Client1
  PUBLIC
    GeodeNative::cpp)
