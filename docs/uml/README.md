# UML diagrams

Hai file PlantUML:
- `class-diagram.puml` — sơ đồ lớp đầy đủ (Entity hierarchy, Strategy, World, View, Event, Stats, Main).
- `package-diagram.puml` — sơ đồ phụ thuộc giữa các nhóm logic.

## Cách render

### Cách 1 — Online (nhanh nhất)
1. Mở https://www.plantuml.com/plantuml/uml
2. Copy nội dung file `.puml` paste vào textarea.
3. Bấm **Submit** → tải PNG hoặc SVG về.

### Cách 2 — Plugin IntelliJ (render trực tiếp trong IDE)
1. `File → Settings → Plugins` → search **PlantUML integration** → Install.
2. Cần cài thêm Graphviz: https://graphviz.org/download/ (Windows installer)
3. Mở file `.puml` → preview hiện ngay bên phải.

### Cách 3 — Command line
```bash
java -jar plantuml.jar class-diagram.puml
```
Tạo file `class-diagram.png` cùng folder.

## Sửa diagram

PlantUML syntax đơn giản — nếu sau này thêm class mới, mở `.puml` add 1-2 dòng:
- `class NewClass` để khai báo
- `Parent <|-- NewClass` để kế thừa
- `User o-- Used` cho aggregation

Cú pháp đầy đủ: https://plantuml.com/class-diagram
