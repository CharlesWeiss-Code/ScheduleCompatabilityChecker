import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class time_match_with_many_schedules extends PApplet {

PrintWriter output;

int scheduleNum = 2;
int scheduleCount = constrain(scheduleNum, 2, 100);
int sci = 0;

boolean[][] schedules = new boolean[scheduleCount][24*60];
boolean[] comp = new boolean[24*60];
ArrayList<ft> freeTimes = new ArrayList<ft>();
ArrayList<ftis> freeTimesForIndividualSchedules = new ArrayList<ftis>();
float thresh = -1;
float threshold = constrain(thresh, -5, 5);

int day = 24*60;
vf vf;

public void setup() {
  output = createWriter("ScheduleData.txt");
  vf = new vf();
  
  println(" ");
  println("Schedule creation iteration: " + sci);
  println(" ");
  fullSchedules();
  freeTimesForIndivSchedules();
  fillComp();
  fillfreeTimes2();
  printCompatability();
  outputStuff();
}

public void draw() {
  background(51);
  vf.show();
  showText();
}

public void fullSchedules() {
  for (int r = 0; r < schedules.length; r++) {
    for (int c = 0; c < schedules[r].length; c++) {
      schedules[r][c] = false;
    }
  }
}


public void randomSchedules() {
  for (int c = 0; c < schedules[0].length; c++) {
    for (int r = 0; r < schedules.length; r++) {
      if (random(10) + threshold > 5) {
        schedules[r][c] = false;
      } else {
        schedules[r][c] = true;
      }
    }
  }
}


public void fixedSchedule() {
  for (int c = 0; c < schedules[0].length; c++) {
    for (int r = 0; r < schedules.length; r++) {
      if (c >= 20 && c <= 500) {
        schedules[r][c] = false;
      } else {
        schedules[r][c] = true;
      }
    }
  }
}



public void fillComp() {
  for (int c = 0; c < schedules[0].length; c++) {
    int TotalColumnFalse = 0;
    for (int r = 0; r < schedules.length; r++) {
      if (schedules[r][c] == false) {
        TotalColumnFalse++;
      }
    }
    if (TotalColumnFalse == schedules.length) {
      comp[c] = true;
    } else {
      comp[c] = false;
    }
  }
}



public void fillfreeTimes2() {
  int start = 0;
  int end = 0;
  for (int i = 0; i < 24*60; i++) {
    // find the starting and ending time for possible meeting times
    if (comp[i] == true) {
      start = i;
      for (int j = i; j < day-1; j++) {
        if (j == 24*60 - 2) {
          end = day;
          if (end - start > 1) {
            freeTimes.add(new ft(start, end));
          }
          i = end;
          break;
        } else if (comp[j] == true && comp[j+1] == false) {
          end = j+1;
          if (end - start > 1) {
            freeTimes.add(new ft(start, end));
          }
          i = j + 1;
          break;
        }
      }
    }
  }
}


public void printCompatability() {
  for (ft f : freeTimes) {
    f.identify();
  }
  if (freeTimes.size() == 0) {
    println("All " + scheduleCount + " schedules have no compatable free time!");
  } else if (freeTimes.size() == 1) {
    println("All " + scheduleCount + " schedules have the entire day free!");
  }
}

public void freeTimesForIndivSchedules() {
  for (int r = 0; r < schedules.length; r++) {
    int end = -1;
    int start = -1;
    int scheduleNum = r;
    for (int c = 0; c < schedules[r].length - 1; c++) {
      if (schedules[r][c] == false) {
        start = c;
        for (int i = c; i < schedules[r].length-1; i++) {
          if (i == day-2) {
            end = day;
            freeTimesForIndividualSchedules.add(new ftis(start, end, scheduleNum));
            c = end;
            end = 0;
            start = 0;
            break;
          } else if (schedules[r][i] == false && schedules[r][i+1] == true) {
            end = i + 1;
            c = i + 1;
            if (end - start > 1) {
              freeTimesForIndividualSchedules.add(new ftis(start, end, scheduleNum));
              start = 0;
              end = 0;
            }
            break;
          }
        }
      }
    }
  }
}

public void invidualSchedules2() {
  int start = 0;
  int end = 0;
  for (int r = 0; r < schedules.length; r++) {
    for (int i = 0; i < 24*60-1; i++) {
      // find the starting and ending time for possible meeting times
      if (comp[i] == true) {
        start = i;
        for (int j = i; j < day-1; j++) {
          if (j+1 == 24*60) {
            end = j+1;
            if (end - start > 1) {
              freeTimesForIndividualSchedules.add(new ftis(start, end, r));
              start = 0;
              end = 0;
            }
            break;
          } else if (comp[j] == true && comp[j+1] == false) {
            end = j+1;
            if (end - start > 1) {
              freeTimesForIndividualSchedules.add(new ftis(start, end, r));
              start = 0;
              end = 0;
            }
            i = end;
            break;
          }
        }
      }
    }
  }
}


public void mousePressed() {
  restart();
  sci++;
}

public void keyPressed() {
  if (keyCode == UP) {
    scheduleNum++;
    sci++;
    if (scheduleNum > 100) {
      scheduleNum = 100;
    }
    restart();
  } else if (keyCode == DOWN) {
    scheduleNum--;
    sci++;
    if (scheduleNum < 2) {
      scheduleNum = 2;
    }
    restart();
  }
  if (key == 'w' || key == 'W') {
    thresh+=.1f;
    sci++;
    if (thresh > 5) {
      thresh = 5;
    }
    restart();
  } else if (key == 's' || key == 'S') {
    thresh-=.1f;
    sci++;
    if (thresh < -5) {
      thresh = -5;
    }
    restart();
  } else if (key == 'f' || key == 'F') {
    thresh+=.05f;
    sci++;
    if (thresh > 5) {
      thresh = 5;
    }
    restart();
  } else if (key == 'j' || key == 'J') {
    thresh-=.05f;
    sci++;
    if (thresh < -5) {
      thresh = -5;
    }
    restart();
  } else if (key == '+') {
    thresh++;
    sci++;
    if (thresh > 5) {
      thresh = 5;
    }
    restart();
  } else if (key == '-') {
    thresh--;
    sci++;
    if (thresh < -5) {
      thresh = -5;
    }
    restart();
  } else if (key == 'e' || key == 'E') {
    output.close();
    exit();
  }
}

public void showText() {
  textSize(32);
  fill(255);
  text(scheduleCount, 25, 100);
  text(nf(threshold,1,2), 5, height/2);
  push();
  textAlign(CENTER);
  String t = "12AM                                       12PM                                       12AM";
  text(t,width/2,height-10);
  String instructions = "Use W, S, UP, DOWN, +, -, and E (exit and save)";
  textSize(30);
  text(instructions, width/2, height- height/3.7f);
  pop();
}

public void restart() {
  println(" ");
  println("Schedule creation iteration: " + sci);
  println(" ");
  freeTimes.clear();
  freeTimesForIndividualSchedules.clear();
  scheduleCount = constrain(scheduleNum, 2, 100);
  threshold = constrain(thresh, -5, 5);
  schedules = new boolean[scheduleCount][24*60];
  // randomSchedules();
  randomSchedules();
  freeTimesForIndivSchedules();
  fillComp();
  fillfreeTimes2();
  printCompatability();
  outputStuff();
  output.println(" ");
  output.println("Schedule creation iteration: " + sci);
  output.println(" ");
  output.flush();
}

public void outputStuff() {
  for (ft f : freeTimes) {
    output.println(f.text);
  }
  if (freeTimes.size() == 0) {
    output.println("All " + schedules.length + " schedules have no compatible free time!");
  } else if (freeTimes.size() == 1) {
    output.println("All " + schedules.length + " schedules have the entire day free!");
  }
}
class ftis {
  int start;
  int end;
  int scheduleNum;

  ftis(int start, int end, int scheduleNum) {
    this.start = start;
    this.end = end;
    this.scheduleNum = scheduleNum;
  }

  public void show() {
    // usable start and end mapped to screen size
    float us = map(start, 0, day, 100, width-100);
    float ue = map(end, 0, day, 100, width-100);

    push();
    fill(0, 255, 0);
    rectMode(CORNERS);
    rect(us, scheduleNum*vf.scl + vf.spacing, ue, (scheduleNum+1)*vf.scl);
    pop();
  }
}
class ft {
  
  int start;
  int end;

  int startHours;
  int startMinutes;

  int endHours;
  int endMinutes;

  String AMPMstart;
  String AMPMend;

  String text;


  ft(int start, int end) {
    this.start = start;
    this.end = end;
  }

  public void identify() {
    startMinutes = start%60;
    startHours = floor(start/60);

    if (startHours >= 12) {
      AMPMstart = "PM";
      if (startHours > 12) {
        startHours-=12;
      }
    } else {
      AMPMstart = "AM";
    }

    endMinutes = end%60;
    endHours = floor(end/60);

    if (endHours >= 12) {
      AMPMend = "PM";
      if (endHours > 12) {
        endHours-=12;
      }
    } else {
      AMPMend = "AM";
    }
    
    int dt = end - start;
    int dMinutes = dt%60;
    int dHours = floor(dt/60);
    text = "All " + scheduleCount + " schedules have free time from " + startHours + ":" + nf(startMinutes, 2, 0) + AMPMstart + " to " + endHours + ":" + nf(endMinutes, 2, 0) + AMPMend + ". A time of " + dHours + " hours and " + dMinutes + " minutes.";
    println(text);
  }

  public String AMorPM(int time) {
    String result = "AM";
    if (time >= 12*60) {
      result = "PM";
    }
    return result;
  }
}
class vf {
  float scl = (height-200)/scheduleCount;
  float spacing = scl/5;

  public void show() {
     scl = (height-200)/scheduleCount;
     spacing = scl/5;
    backgroundSchedules();
    finalCompatability();
    individualSchedules();
  }

  public void finalCompatability() {
    push();
    fill(255, 0, 0);
    rectMode(CORNERS);
    //rect(100, height-150, width-100, height-50);
    pop();
    for (ft f : freeTimes) {
      float usableStart = map(f.start, 0, 1440, 100, width-100);
      float usableEnd = map(f.end, 0, 1440, 100, width-100);
      push();
      rectMode(CORNERS);
      fill(0, 255, 0);
      rect(usableStart, height-150, usableEnd, height-50);
      pop();
    }
  }

  public void individualSchedules() {
    for (ftis f : freeTimesForIndividualSchedules) {
      f.show();
    }
  }

  public void backgroundSchedules() {
    for (int i = 0; i < scheduleCount; i++) {
      push();
      fill(255, 0, 0);
      //rect(100, i*scl + spacing, width-200, scl - spacing);
      pop();
    }
  }
} 
  public void settings() {  size(1200, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "time_match_with_many_schedules" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
