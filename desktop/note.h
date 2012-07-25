#ifndef _NOTE_
#define _NOTE_
#include <cstring>
#include <string>

class Note {
    public:
        int setTitle(char *Title);
        int setText(std::string Text);
        int setPriority(char *Priority);
        Note(std::string Text) { text+=Text; };
        ~Note() {};
        char *getTitle();
        std::string getText();
        char *getPriority();
    private:
        char title[200];
        char priority[200];
        std::string text;
};
#endif
