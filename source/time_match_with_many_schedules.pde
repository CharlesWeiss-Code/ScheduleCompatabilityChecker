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
  size(1200, 600);
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
    thresh+=.1;
    sci++;
    if (thresh > 5) {
      thresh = 5;
    }
    restart();
  } else if (key == 's' || key == 'S') {
    thresh-=.1;
    sci++;
    if (thresh < -5) {
      thresh = -5;
    }
    restart();
  } else if (key == 'f' || key == 'F') {
    thresh+=.05;
    sci++;
    if (thresh > 5) {
      thresh = 5;
    }
    restart();
  } else if (key == 'j' || key == 'J') {
    thresh-=.05;
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
  text(instructions, width/2, height- height/3.7);
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
