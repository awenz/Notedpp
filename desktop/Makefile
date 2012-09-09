CFLAGS := -Wall -Wextra -pedantic -O2 -g -D_REENTRANT $(CFLAGS)
LDFLAGS := -lsqlite3 -lm $(LDFLAGS)

noted: notedpp.o note.o
	g++ -o notedpp notedpp.o note.o -lsqlite3

notedpp.o: notedpp.cpp
	g++ -c notedpp.cpp

note.o: note.cpp note.h
	g++ -c note.cpp

clean:
	$(RM) notedpp notedpp.o note.o 
