Drop sprite PNG files here.

Single-frame naming (just one image per species):
  rabbit.png
  deer.png
  wolf.png
  tiger.png
  elephant.png
  grass.png
  fruit_tree.png
  fish.png
  duck.png

Animation (multiple frames, cycles ~6 fps):
  rabbit_0.png, rabbit_1.png, rabbit_2.png, ...
  (up to rabbit_7.png)

If no file is found for a species, SpriteRenderer falls back to procedural shapes.

IntelliJ note: ensure src/ is marked as Sources Root and Settings -> Build, Execution,
Deployment -> Compiler -> Resource Patterns includes *.png (it should by default).
