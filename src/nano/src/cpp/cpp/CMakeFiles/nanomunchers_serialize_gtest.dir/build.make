# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 2.8

#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canoncical targets will work.
.SUFFIXES:

# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list

# Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:
.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/bin/cmake

# The command to remove a file.
RM = /usr/bin/cmake -E remove -f

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /home/roy/src/nano/src

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/roy/src/nano/src/cpp

# Include any dependencies generated for this target.
include cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/depend.make

# Include the progress variables for this target.
include cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/progress.make

# Include the compile flags for this target's objects.
include cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/flags.make

cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/nanomunchers_serialize_gtest.cpp.o: cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/flags.make
cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/nanomunchers_serialize_gtest.cpp.o: nanomunchers_serialize_gtest.cpp
	$(CMAKE_COMMAND) -E cmake_progress_report /home/roy/src/nano/src/cpp/CMakeFiles $(CMAKE_PROGRESS_1)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building CXX object cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/nanomunchers_serialize_gtest.cpp.o"
	cd /home/roy/src/nano/src/cpp/cpp && /usr/bin/c++   $(CXX_DEFINES) $(CXX_FLAGS) -o CMakeFiles/nanomunchers_serialize_gtest.dir/nanomunchers_serialize_gtest.cpp.o -c /home/roy/src/nano/src/cpp/nanomunchers_serialize_gtest.cpp

cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/nanomunchers_serialize_gtest.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/nanomunchers_serialize_gtest.dir/nanomunchers_serialize_gtest.cpp.i"
	cd /home/roy/src/nano/src/cpp/cpp && /usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -E /home/roy/src/nano/src/cpp/nanomunchers_serialize_gtest.cpp > CMakeFiles/nanomunchers_serialize_gtest.dir/nanomunchers_serialize_gtest.cpp.i

cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/nanomunchers_serialize_gtest.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/nanomunchers_serialize_gtest.dir/nanomunchers_serialize_gtest.cpp.s"
	cd /home/roy/src/nano/src/cpp/cpp && /usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -S /home/roy/src/nano/src/cpp/nanomunchers_serialize_gtest.cpp -o CMakeFiles/nanomunchers_serialize_gtest.dir/nanomunchers_serialize_gtest.cpp.s

cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/nanomunchers_serialize_gtest.cpp.o.requires:
.PHONY : cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/nanomunchers_serialize_gtest.cpp.o.requires

cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/nanomunchers_serialize_gtest.cpp.o.provides: cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/nanomunchers_serialize_gtest.cpp.o.requires
	$(MAKE) -f cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/build.make cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/nanomunchers_serialize_gtest.cpp.o.provides.build
.PHONY : cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/nanomunchers_serialize_gtest.cpp.o.provides

cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/nanomunchers_serialize_gtest.cpp.o.provides.build: cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/nanomunchers_serialize_gtest.cpp.o

# Object files for target nanomunchers_serialize_gtest
nanomunchers_serialize_gtest_OBJECTS = \
"CMakeFiles/nanomunchers_serialize_gtest.dir/nanomunchers_serialize_gtest.cpp.o"

# External object files for target nanomunchers_serialize_gtest
nanomunchers_serialize_gtest_EXTERNAL_OBJECTS =

cpp/nanomunchers_serialize_gtest: cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/nanomunchers_serialize_gtest.cpp.o
cpp/nanomunchers_serialize_gtest: cpp/libnanomunchers_serialize.a
cpp/nanomunchers_serialize_gtest: gtest-1.6.0/libgtest.a
cpp/nanomunchers_serialize_gtest: cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/build.make
cpp/nanomunchers_serialize_gtest: cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --red --bold "Linking CXX executable nanomunchers_serialize_gtest"
	cd /home/roy/src/nano/src/cpp/cpp && $(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/nanomunchers_serialize_gtest.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/build: cpp/nanomunchers_serialize_gtest
.PHONY : cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/build

cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/requires: cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/nanomunchers_serialize_gtest.cpp.o.requires
.PHONY : cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/requires

cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/clean:
	cd /home/roy/src/nano/src/cpp/cpp && $(CMAKE_COMMAND) -P CMakeFiles/nanomunchers_serialize_gtest.dir/cmake_clean.cmake
.PHONY : cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/clean

cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/depend:
	cd /home/roy/src/nano/src/cpp && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/roy/src/nano/src /home/roy/src/nano/src/cpp /home/roy/src/nano/src/cpp /home/roy/src/nano/src/cpp/cpp /home/roy/src/nano/src/cpp/cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : cpp/CMakeFiles/nanomunchers_serialize_gtest.dir/depend

