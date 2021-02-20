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
