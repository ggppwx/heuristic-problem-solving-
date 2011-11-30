CC=g++
CFLAGS=-c -g

all:sudoPlayer


sudoPlayer:player.o sudoPlayer.o
	$(CC) player.o sudoPlayer.o -o sudoPlayer

player.o:player.cpp
	$(CC) $(CFLAGS) player.cpp

sudoPlayer.o:sudoPlayer.cpp
	$(CC) $(CFLAGS) sudoPlayer.cpp

clean:
	rm -rf *.o sudoPlayer