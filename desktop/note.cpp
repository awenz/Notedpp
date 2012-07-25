#include <iostream>
#include <cstdlib>
#include <cstring>
#include <string>
#include "note.h"
using namespace std;

int Note::setTitle(char* Title){
    strcpy(title,Title);
    return 0;
}

int Note::setText(string Text){
    text+=Text;
    return 0;
}

int Note::setPriority(char* Priority){
    strcpy(priority,Priority);
    return 0;
}

char* Note::getTitle(void){
    return title;
}

string Note::getText(void){
    return text;
}

char* Note::getPriority(void){
    return priority;
}
