class Obstacle {
  PVector pos;
  float w, h;

  Obstacle(float x, float y, float _w, float _h) {
    pos = new PVector(x, y);
    w = _w;
    h = _h;
  }

  void display() {
    stroke(0);
    fill(175);
    strokeWeight(2);
    rectMode(CORNER);
    rect(pos.x, pos.y, w, h);
  }

  boolean contains(PVector v) {
    if (v.x > pos.x && v.x < pos.x + w && v.y > pos.y && v.y < pos.y + h) {
      return true;
    } else {
      return false;
    }
  }
}
