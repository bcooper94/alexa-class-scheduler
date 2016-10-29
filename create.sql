CREATE TABLE Courses {
   department TEXT,
   course_number INTEGER,
   section INTEGER,
   prof_first_name TEXT,
   prof_last_name TEXT,
   requirement TEXT,
   type TEXT,
   days TEXT,
   start TEXT,
   end TEXT,
   quarter TEXT,
   year INTEGER,
   location TEXT,
   PRIMARY KEY(department, course_number, section, quarter, year)
}
