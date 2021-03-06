#include <iostream>
#include <cstdlib>
#include <cstring>
#include <string>
#include <sqlite3.h>
#include <iomanip>
#include "note.h"
using namespace std;

void create_table(sqlite3*);
void create_note(Note, sqlite3*, string);
void list_notes(sqlite3*, sqlite3_stmt*);
void delete_note(char* note_id, string tmp, sqlite3* handle);
int  get_config_path(char, size_t);
int  parse_configfile(void);

void print_usage(void){
  cout.width(40);
  cout << setiosflags(ios::left) << "Usage: notedpp -[option] Priority Text"
       << resetiosflags(ios::left)
       << setw(20) << "\noptions: \n -n new note\n -l list notes\n -s search notes\n -d delete note by id\n";
}

int get_config_path(char *config_path, size_t pathlen){
  char *var;
  struct passwd *pwd;

  var = getenv("XDG_CONFIG_HOME");
  if(var != NULL) {
    snprintf(config_path, pathlen, "%s/noted/config", var);
    return 0;
  }

  var = getenv("HOME");
  if(var != NULL) {
    snprintf(config_path, pathlen, "%s/.config/noted/config", var);
    return 0;
  }

  return 1;
}

int parse_configfile(void) {
  char line[1024], config_path[1024];
  int retval = 0;
  FILE *fp;

  if(get_config_path(config_path, sizeof(config_path)) != 0) {
    return 0;
  }

  fp = fopen(config_path, "r");
  if(!fp) {
    cout << "Configfile not found\n";
    return 0;
  }

  while(fgets(line, PATH_MAX, fp)) {
    char *key, *val;
    size_t linelen;

    linelen = strtrim(line);
    if(!linelen || line[0] == '#') {
      continue;
    }

    if((val = strchr(line, '#'))) {
      *val = '\0';
    }

    key = val = line;
    strsep(&val, "=");
    strtrim(key);
    strtrim(val);

    if(val && !*val) {
      val = NULL;
    }

    if(streq(key, "DropBox")) {
      for(key = strtok(val, " "); key; key = strtok(NULL, " ")) {
      }
    }
  }

  return 1;
}

void create_note(Note *note,sqlite3 *handle,string tmp, char* db_path){
  string query_start="INSERT INTO notes (_id,note) VALUES(NULL,'";
  string query_end="')";
  const char* sql;
  int retval;
  retval = sqlite3_open(db_path,&handle);
  if(retval){
    cout << "Database connection failed!\n";
    exit(EXIT_FAILURE);
  }
  cout << "Database connection successful!\n";
  create_table(handle);
  cout << "Table Creation Successful!\n";
  tmp+=query_start;
  tmp+=note->getText();
  tmp+=query_end;
  sql = tmp.c_str();
  cout << tmp << "\n";
  cout << "String construction complete!\n";
  retval = sqlite3_exec(handle,sql,0,0,0);
  if(retval != SQLITE_OK){
    cout << "Note creation failed!\n";
    exit(0);
  }
  cout << "Note created!\n";
  sqlite3_close(handle);
}

void create_table(sqlite3 *handle){
  const char* create_table = "CREATE TABLE IF NOT EXISTS notes (_id INTEGER PRIMARY KEY, note TEXT NOT NULL)";
  int retval = sqlite3_exec(handle,create_table,0,0,0);
  if(retval){
    cout << "Table Creation Failed!\n";
    exit(EXIT_FAILURE);
  }
}

void delete_note(char* note_id, string tmp, sqlite3 *handle, char* db_path){
  int retval = sqlite3_open(db_path,&handle);
  const char* sql;
  if(retval){
    cout << "Database connection failed!\n";
    exit(EXIT_FAILURE);
  }
  string del_start = "DELETE FROM notes WHERE _id='";
  string del_end   = "'";
  tmp+=del_start;
  tmp+=note_id;
  tmp+=del_end;
  sql = tmp.c_str();
  retval = sqlite3_exec(handle,sql,0,0,0);
  if(retval){
    cout << "Note deletion failed!\n";
    exit(EXIT_FAILURE);
  }
  sqlite3_close(handle);
}

void list_notes(sqlite3 *handle, sqlite3_stmt *stmt,char* db_path){
  const char *select= "SELECT * from notes";
  int cols,col;
  int retval = sqlite3_open(db_path,&handle);
  if(retval){
    cout << "Database Connection failed!\n";
    exit(EXIT_FAILURE);
  }
  retval = sqlite3_prepare_v2(handle,select,-1,&stmt,0);
  if(retval){
    cout << "Select Data from DB failed!\n";
    exit(EXIT_FAILURE);
  }
  cols = sqlite3_column_count(stmt);
  while(1)
    {
      retval = sqlite3_step(stmt);
      if(retval == SQLITE_ROW)
        {
          for(col=0;col<cols;col++)
            {
              const char *val = (const char*)sqlite3_column_text(stmt,col);
              cout << val;
            }
          cout << "\n";
        }
      else if(retval == SQLITE_DONE)
        {
          cout.width(40);
          cout.fill('-');
          sqlite3_finalize(stmt);
          break;
        }
      else
        {
          cout << "Database Error!\n";
          sqlite3_finalize(stmt);
        }
    }
  sqlite3_close(handle);
}

int main(int argc, char** argv){
  sqlite3 *handle;
  sqlite3_stmt *stmt;
  string tmp;
  string text;
  const char* env = getenv("HOME");
  const char* db = "/.noteddb.sqlite";
  char* db_path = (char*)malloc(strlen(env)+strlen(db));
  strcpy(db_path,env);
  strcat(db_path,db);
  int i;
  for(i=2;i<argc;++i)
    {
      text+=argv[i];
      text+=" ";
    }
  cout << text;
  cout << " " << argc << "\n";
  char c;
  Note *note;
  if(argc == 1){
    print_usage();
    exit(EXIT_FAILURE);
  }
  while(--argc > 0 && (*++argv)[0] == '-')
    while (c = *++argv[0])
      switch(c) {
      case 'h':
        print_usage();
        break;
      case 'n':
        cout << "Creating Note ... \n";
        note = new Note(text);
        create_note(note,handle,tmp,db_path);
        delete note;
        break;
      case 'l':
        list_notes(handle,stmt,db_path);
        break;
      case 'd':
        delete_note(argv[1],tmp,handle,db_path);
        break;
      }
  free(db_path);
  return 0;
}
