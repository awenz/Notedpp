noted: notedpp.o note.o
	g++ -o notedpp notedpp.o note.o -lsqlite3

notedpp.o: notedpp.cpp
	g++ -c notedpp.cpp

note.o: note.cpp note.h
	g++ -c note.cpp

clean:
	$(RM) notedpp notedpp.o note.o 
