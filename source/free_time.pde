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
