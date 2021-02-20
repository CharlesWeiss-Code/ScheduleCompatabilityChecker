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
