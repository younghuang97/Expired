CC=g++
CXXFLAGS=-std=c++11 -Wall -g

all: test

test: Fridge.o Item.o
	$(CC) $(CXXFLAGS) test.cpp Fridge.o Item.o -o test

Fridge.o: Fridge.h
	$(CC) $(CXXFLAGS) -c Fridge.cpp -o Fridge.o

Item.o: Item.h
	$(CC) $(CXXFLAGS) -c Item.cpp -o Item.o

clean:
	rm -f *.exe *.o core*