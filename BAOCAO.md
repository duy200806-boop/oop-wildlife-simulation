# Báo cáo — Hệ thống Mô phỏng Hệ sinh thái Hoang dã

> Tài liệu nội bộ phục vụ viết báo cáo chính thức (Word). Đọc từ trên xuống.
> Mục UML (sơ đồ lớp, sơ đồ gói) sẽ được bổ sung sau khi vẽ.

---

## 1. Tổng quan dự án

Dự án mô phỏng một hệ sinh thái hoang dã gồm thực vật, động vật ăn cỏ, động vật ăn thịt và loài đầu bảng (apex), tương tác trong một bản đồ gồm đồng cỏ, rừng rậm, bụi rậm, hồ nước và bùn lầy. Bản đồ được sinh ngẫu nhiên bằng thuật toán Cellular Automata, các loài có cơ chế sinh tồn (đói, khát, đẻ con, săn mồi, chạy trốn) độc lập theo các Chiến lược (Strategy) có thể hoán đổi tại runtime.

Mục tiêu chính của project:
- Áp dụng đầy đủ các kỹ thuật OOP (kế thừa, đa hình, đóng gói, trừu tượng).
- Áp dụng các mẫu thiết kế kinh điển: Strategy, Observer, Template Method, Composite.
- Tách biệt rõ ràng phần xử lý sinh học (BioLogic / Model) khỏi phần hiển thị (ViewLogic / JavaFX).
- Cho phép mở rộng dễ dàng (thêm loài mới, thêm chiến lược mới, thêm chế độ hiển thị mới).

---

## 2. Hướng dẫn sử dụng

### 2.1. Yêu cầu môi trường
- **JDK Oracle 1.8** (đã bundled JavaFX với native media libs).
  Không dùng Amazon Corretto hoặc OpenJDK 8 vì thiếu thư viện native của JavaFX media (lỗi `glib-lite.dll`).
- IDE: **IntelliJ IDEA** (khuyên dùng) hoặc Eclipse, NetBeans.

### 2.2. Cách chạy
1. Mở folder project bằng IntelliJ IDEA.
2. `File → Project Structure → Project → SDK` → chọn Oracle JDK 1.8.
3. Project view → chuột phải `src` → `Mark Directory as → Sources Root`.
4. Mở `src/Main.java` → bấm Run (▶).

### 2.3. Tương tác trong chương trình

| Hành động | Tác dụng |
|---|---|
| Click chuột trái | Gieo cỏ tại vị trí click (không gieo được trên ô WATER) |
| Click chuột phải | Đặt vách đá (ROCK) — không loài nào vượt qua được |
| Nút **Renderer: Basic / Sprite** | Đổi giữa 2 chế độ hiển thị |
| Nút **Zoom + / Zoom - / Reset** | Phóng to / thu nhỏ / về tỉ lệ 1.0 |
| Nút **Season: NORMAL / DROUGHT / BREEDING** | Chuyển chu kỳ mùa |
| Nút **Export CSV** | Xuất dữ liệu thống kê quần thể (xem mục 3) |

---

## 3. Thống kê dữ liệu thu thập được

### 3.1. Cách hệ thống thu thập

Mỗi giây, `StatisticsCollector` chụp một snapshot toàn bộ thế giới bao gồm: số lượng từng loài còn sống và bộ đếm cộng dồn các sự kiện (tấn công, ăn, chết). Đồng thời lớp này đăng ký vào `EventBus` (Observer pattern) nên tự động cập nhật số liệu khi có sự kiện xảy ra, không cần can thiệp vào code chính.

### 3.2. Cấu trúc file CSV xuất ra

Bấm nút **Export CSV** trên thanh điều khiển. File được tạo ra trong thư mục project với tên dạng `stats-<timestamp>.csv`, mở được bằng Excel hoặc Google Sheets.

Các cột:

| Cột | Ý nghĩa | Kiểu |
|---|---|---|
| `time` | Thời điểm (giây) tính từ khi chương trình khởi động | double |
| `rabbits` | Số thỏ còn sống tại thời điểm đó | int |
| `deer` | Số hươu còn sống | int |
| `wolves` | Số sói còn sống | int |
| `tigers` | Số hổ còn sống | int |
| `elephants` | Số voi còn sống | int |
| `grass` | Số cỏ còn sống | int |
| `attacks` | Tổng số lần thú ăn thịt vồ mồi (cộng dồn) | int |
| `eats` | Tổng số lần thú ăn cỏ ăn cỏ (cộng dồn) | int |
| `deaths` | Tổng số lần animal chết đói/khát (cộng dồn) | int |

### 3.3. Bảng dữ liệu mẫu

Chạy chương trình trong khoảng 3 phút (chế độ NORMAL), một số mẫu snapshot có dạng:

| time | rabbits | deer | wolves | tigers | elephants | grass | attacks | eats | deaths |
|---|---|---|---|---|---|---|---|---|---|
| 0.0 | 60 | 20 | 6 | 3 | 2 | 100 | 0 | 0 | 0 |
| 30.0 | 58 | 19 | 6 | 3 | 2 | 95 | 2 | 28 | 0 |
| 60.0 | 57 | 19 | 6 | 3 | 2 | 102 | 4 | 51 | 0 |
| 120.0 | 54 | 18 | 7 | 3 | 2 | 118 | 9 | 95 | 2 |
| 180.0 | 56 | 19 | 7 | 4 | 2 | 131 | 14 | 142 | 3 |

> Số liệu trên là tham khảo, sẽ thay đổi mỗi lần chạy do tính ngẫu nhiên (sinh terrain CA + random direction + breeding ngẫu nhiên).

### 3.4. Sử dụng dữ liệu cho báo cáo

Mở CSV bằng Excel/Google Sheets → chọn cột `time` + các cột population → Insert Chart → Line Chart. Sẽ thấy quần thể dao động theo mô hình Lotka-Volterra (predator-prey cycles): khi thỏ tăng → sói tăng → thỏ giảm → sói giảm → thỏ tăng trở lại.

Có thể chạy nhiều scenario:
- **Scenario A — Bình thường:** Chạy với NORMAL toàn thời gian.
- **Scenario B — Hạn hán:** Bấm sang DROUGHT sau 30 giây → quan sát thực vật giảm, động vật chậm hơn, tử vong tăng.
- **Scenario C — Sinh sản:** Bấm sang BREEDING → quan sát quần thể tăng vọt.

---

## 4. Giải thích thiết kế

### 4.1. Cấu trúc thư mục

Project hiện tổ chức **flat trong default package** (toàn bộ class nằm trực tiếp trong `src/`), do giới hạn của Java 8 + IntelliJ default project layout (không Maven). Tuy nhiên về **mặt logic**, các class được phân nhóm rõ ràng theo vai trò:

```
src/
├── Main.java                 ← Entry point + GUI controller + Click handler
│
├── [Model — BioLogic, không phụ thuộc JavaFX]
│   ├── Entity.java           ← Lớp gốc trừu tượng cho mọi thực thể
│   ├── Plant.java            ← Lớp trừu tượng cho thực vật (có cơ chế reproduce)
│   ├── Grass.java
│   ├── FruitTree.java
│   ├── Animal.java           ← Lớp trừu tượng động vật (đói/khát/strategy/sprint)
│   ├── Herbivore.java        ← Trừu tượng cho ăn cỏ (canEat = Plant + breeding)
│   ├── Rabbit.java
│   ├── Deer.java
│   ├── Carnivore.java        ← Trừu tượng ăn thịt (canEat = Herbivore + sprint + breeding)
│   ├── Wolf.java
│   ├── Tiger.java
│   ├── ApexEntity.java       ← Trừu tượng đầu bảng (không tránh ai)
│   ├── Elephant.java
│   ├── WaterAnimal.java      ← Trừu tượng dưới nước (chỉ canEnter WATER)
│   ├── Fish.java
│   ├── Duck.java
│   ├── World.java            ← Quản lý grid terrain + entity list + tick loop
│   ├── Terrain.java          ← Enum địa hình (GRASS, FOREST, BUSH, WATER, MUD, ROCK)
│   └── Season.java           ← Enum mùa (NORMAL, DROUGHT, BREEDING)
│
├── [Strategy — Pattern]
│   ├── SurvivalStrategy.java ← Interface
│   ├── PassiveStrategy.java
│   ├── ScaredStrategy.java
│   ├── HunterStrategy.java
│   ├── AggressiveStrategy.java
│   ├── ApexStrategy.java
│   └── ThirstyStrategy.java
│
├── [View — JavaFX rendering]
│   ├── Renderer.java         ← Interface
│   ├── BasicRenderer.java
│   ├── SpriteRenderer.java
│   └── Camera.java
│
├── [Event — Observer pattern]
│   ├── EventBus.java
│   ├── EventType.java
│   ├── EventListener.java
│   └── AudioSystem.java
│
├── [Stats]
│   └── StatisticsCollector.java
│
└── resources/audio/          ← roar.wav, eat.wav, bird.wav, leaves.wav
```

### 4.2. Các lớp chính (tóm tắt vai trò)

| Lớp | Vai trò |
|---|---|
| `Entity` | Lớp gốc cho mọi thực thể: vị trí (x, y), trạng thái sống/chết, method `update(dt, world)` trừu tượng. |
| `Plant` | Trừu tượng — có cơ chế sinh sản: định kỳ tạo `createChild()` ở vị trí lân cận. |
| `Animal` | Trừu tượng — chứa hunger, thirst, strategy, sprint. Update mỗi tick: tăng đói/khát → check chết → chọn brain (`pickBrain`) → strategy.act → separation → move → tryEat → tryDrink. |
| `Herbivore` | Trừu tượng — `canEat(Plant)`, cơ chế `breedTimer` khi no đủ (`hunger < 0.4`) tự đẻ con. |
| `Carnivore` | Trừu tượng — `canEat(Herbivore)`, có stamina + sprint x1.7 khi gần mồi, không vào BUSH/WATER, có breeding chậm hơn herbivore. |
| `ApexEntity` | Trừu tượng — không bị Herbivore/Carnivore tránh, không sợ ai. |
| `WaterAnimal` | Trừu tượng — chỉ vào được WATER, không đói/khát. |
| `SurvivalStrategy` | Interface — `act(self, world, dt)` quyết định hướng đi mỗi tick. |
| `World` | Lưu grid terrain + list entity + EventBus + Season. Tick toàn bộ entity, cleanup dead, phát ambient event (chim/lá). |
| `Camera` | Lưu zoom, áp transform vào GraphicsContext, convert toạ độ màn hình ↔ thế giới. |
| `Renderer` | Interface — `render(gc, world, camera)`. Có 2 implementation: `BasicRenderer` (hình tròn đơn giản), `SpriteRenderer` (chi tiết hơn). |
| `EventBus` | Pub/sub đơn giản. Có `subscribe(type, listener)` và `publish(type)`. |
| `AudioSystem` | Listen mọi event type, play AudioClip tương ứng (hoặc log console nếu không có file). |
| `StatisticsCollector` | Listen event + chụp snapshot population mỗi giây. Có method `exportCsv(path)`. |
| `Main` | Entry point JavaFX. Khởi tạo World + spawn entity + tạo Canvas + setup ControlPanel + AnimationTimer loop. |

---

## 5. Các kỹ thuật OOP đã áp dụng

> Với mỗi kỹ thuật: **Vị trí** trong code + **Lợi ích** cụ thể đem lại.

### 5.1. Kế thừa (Inheritance)

**Vị trí:** Toàn bộ hierarchy `Entity → Plant/Animal → ... → loài cụ thể`.

**Lợi ích:** Code chung của Animal (hunger, thirst, move, strategy) viết một lần, được tất cả Rabbit/Deer/Wolf/Tiger/... tự động thừa kế. Khi cần thêm loài mới (ví dụ `Hawk` extends Carnivore), chỉ cần khai báo constructor + `createChild`, không phải sao chép logic.

### 5.2. Đa hình (Polymorphism)

**Vị trí:**
- `World.tick()` gọi `e.update(dt, this)` — mỗi Entity tự xử lý theo lớp con thật của nó.
- `Animal.canEat(e)`, `Animal.canEnter(t)` — Carnivore override khác Herbivore khác WaterAnimal.
- `Renderer.render()` — Main không cần biết là Basic hay Sprite.

**Lợi ích:** Code gọi luôn ở mức trừu tượng nhất, không có chuỗi `if instanceof` dài, dễ thêm/thay loại.

### 5.3. Đóng gói (Encapsulation)

**Vị trí:** Các field như `x`, `y`, `hunger`, `thirst` của Animal đều `protected`; có public getter (`getX`, `getY`, `getHunger`). `World.grid` là `private final`, chỉ expose qua `getTile()`, `setTileAt()`, `getTerrainAt()`.

**Lợi ích:** Lớp ngoài không tuỳ tiện sửa trạng thái nội bộ, dễ kiểm soát invariants (ví dụ `hunger` không âm).

### 5.4. Trừu tượng (Abstraction)

**Vị trí:**
- Lớp abstract: `Entity`, `Plant`, `Animal`, `Herbivore`, `Carnivore`, `ApexEntity`, `WaterAnimal`.
- Interface: `SurvivalStrategy`, `Renderer`, `EventListener`.

**Lợi ích:** Buộc người viết loài mới phải implement các method cốt lõi (`createChild`, `update`), trong khi vẫn được dùng chung khung xử lý.

### 5.5. Strategy Pattern (Mẫu chiến lược)

**Vị trí:** Interface `SurvivalStrategy` + các implementation:
- `PassiveStrategy` — đi lang thang
- `ScaredStrategy` — chạy trốn carnivore/apex (composition với PassiveStrategy fallback)
- `HunterStrategy` — đuổi con mồi, đặt cờ sprint khi đủ gần
- `AggressiveStrategy` — bất chấp lao tới thức ăn khi đói cực
- `ThirstyStrategy` — tìm hồ nước gần nhất
- `ApexStrategy` — đi nghênh ngang, đổi hướng chậm

Trong `Animal.pickBrain()`, chiến lược được **chọn lại mỗi tick** theo thứ tự ưu tiên: thirst > hunger > strategy mặc định. Đây là **hot-swap strategy** mà đề bài yêu cầu.

**Lợi ích:**
- Thay đổi hành vi loài chỉ bằng cách đổi 1 dòng `strategy = new XxxStrategy()`, không sửa class loài.
- Khi cần thêm chiến lược mới (ví dụ `HerdStrategy` — đi theo bầy), chỉ cần thêm 1 file, không sửa code cũ.
- Test riêng từng chiến lược không phải dựng cả Animal.

### 5.6. Observer Pattern (Mẫu quan sát viên)

**Vị trí:** `EventBus` quản lý map `EventType → List<EventListener>`. Khi sự kiện xảy ra (`world.getEventBus().publish(EventType.ATTACK)`), tất cả listener đăng ký đều nhận được. Hai listener hiện có:
- `AudioSystem` — phát âm thanh tương ứng.
- `StatisticsCollector` — cộng dồn vào counter để export CSV.

**Lợi ích:**
- Code gốc (`Animal.tryEat`, `Animal.update`) không cần biết audio hoặc stats tồn tại.
- Có thể thêm listener mới (ví dụ ghi log vào file, hiển thị notification trên màn hình) mà không sửa code gốc.

### 5.7. Template Method (Phương thức khuôn mẫu)

**Vị trí:** `Animal.update(dt, world)` định nghĩa **trình tự cố định** mỗi tick:
1. Tăng đói + khát.
2. Check chết.
3. Reset sprinting.
4. Chọn brain bằng `pickBrain()`.
5. Brain quyết định hướng (`strategy.act`).
6. Apply separation (steering).
7. Move (con cháu có thể override để thêm sprint factor).
8. tryEat.
9. tryDrink.

Các bước cố định; lớp con (`Herbivore`, `Carnivore`) override `update` để **thêm** hành vi breeding sau khi gọi `super.update()` — không thay đổi khung xử lý chính.

**Lợi ích:** Đảm bảo mọi animal đều theo cùng một vòng đời, không bị bỏ sót bước (ví dụ quên uống nước).

### 5.8. Composite Pattern (Mẫu hợp thể)

**Vị trí:** `World` chứa đồng thời `Terrain[][] grid` (lưới ô) và `List<Entity> entities` (thực thể trong các ô). Cả hai cùng được tick/render thông qua một interface chung (`World.tick`, `Renderer.render(gc, world, camera)`).

**Lợi ích:** Người dùng World không cần biết entity nằm ở ô nào — World tự xử lý qua `getTerrainAt(x, y)`.

### 5.9. Tách BioLogic vs ViewLogic (MVC)

**Vị trí:**
- Model (`Entity`, `Plant`, `Animal`, `World`, `SurvivalStrategy`, `Terrain`, `Season`, ...) **tuyệt đối không import** `javafx.*`. Có thể compile + chạy headless để test.
- View (`Renderer`, `BasicRenderer`, `SpriteRenderer`, `Camera`) chỉ phụ thuộc Model, không biết user click gì.
- Controller (`Main` + lambda trong control panel) gắn input → world.

**Lợi ích:**
- Có thể đổi sang Swing hoặc console rendering mà không sửa Model.
- Đảm bảo logic sinh học không bị "ô nhiễm" bởi pixel/canvas.

---

## 6. Công nghệ sử dụng

| Công nghệ | Vai trò |
|---|---|
| **Java 8 (Oracle JDK 1.8)** | Ngôn ngữ + runtime, bundled JavaFX. |
| **JavaFX (bundled)** | `Application`, `Canvas`, `GraphicsContext`, `AnimationTimer`, `AudioClip`, `Stage`, `Scene`, `BorderPane`, `HBox`, `Button`. |
| **IntelliJ IDEA** | IDE phát triển. |
| **Git / GitHub** | Quản lý phiên bản, đồng bộ nhóm: https://github.com/duy200806-boop/oop-wildlife-simulation |
| **WAV (PCM)** | Định dạng âm thanh do JavaFX AudioClip yêu cầu. |
| **CSV** | Định dạng xuất dữ liệu thống kê (mở bằng Excel/Google Sheets). |

---

## 7. Thuật toán đáng chú ý

### 7.1. Cellular Automata cho terrain

**Mục đích:** Sinh các mảng địa hình hữu cơ (rừng, hồ, bụi, bùn) với đường viền uốn lượn tự nhiên thay vì hình chữ nhật cứng.

**Cách hoạt động:**
1. Khởi tạo grid ngẫu nhiên với mật độ X% cho từng loại địa hình (`forest 50%`, `water 55%`, ...) trong một vùng giới hạn (ví dụ `water` chỉ trong góc dưới-phải).
2. Smooth bằng CA: lặp N lần (4-5 lần), mỗi cell trở thành `true` nếu **≥4 hàng xóm** là `true`, ngược lại thành `false`. Quy tắc tương tự cave-generation.
3. Overlay lần lượt: WATER → FOREST → BUSH → MUD, mỗi tầng chỉ ghi đè lên GRASS để không phá vùng đã sinh.

**Vị trí code:** `World.generateTerrain()`, `World.smoothCa()`, `World.neighborCount()`.

### 7.2. Hot-swap Strategy ưu tiên (pickBrain)

**Mục đích:** Mỗi tick, chọn chiến lược hành vi phù hợp với trạng thái sinh học hiện tại của animal.

**Cách hoạt động:**
```
if (thirst > 0.7)  → ThirstyStrategy
else if (hunger > 0.7) → AggressiveStrategy
else                → strategy mặc định (Scared / Hunter / Apex / Passive)
```

Không cần "lưu strategy gốc" và "swap qua lại" như trước. Brain được **tính lại** mỗi frame, đảm bảo phản ứng tức thì khi thirst/hunger thay đổi.

**Vị trí code:** `Animal.update()`, `Animal.pickBrain()`.

### 7.3. Steering Separation (lực dạt sang một bên)

**Mục đích:** Khi animal nhỏ va chạm với animal lớn (carnivore hoặc apex), tự động đẩy sang bên để "nhường đường".

**Cách hoạt động:** Trong phạm vi 35px, mỗi animal nhỏ tính tổng vector đẩy từ mọi mối nguy ở gần (đẩy ra theo hướng ngược lại của mối nguy, độ mạnh tăng khi càng gần). Vector này được cộng vào hướng đi hiện tại, kết quả là góc đổi lệch sang một bên thay vì đi thẳng vào mối nguy.

**Vị trí code:** `Animal.applySeparation()`.

### 7.4. Sprint với thanh thể lực (Stamina)

**Mục đích:** Sói/Hổ tăng tốc x1.7 khi đuổi mồi nhưng không thể chạy mãi.

**Cách hoạt động:**
- `HunterStrategy` đặt cờ `sprinting = true` chỉ khi mồi cách <100px (không sprint suốt từ tầm xa 200px).
- `Carnivore.move()`: nếu `sprinting && stamina > 0`, áp dụng nhân tốc 1.7 và giảm stamina 0.3/giây. Ngược lại, stamina hồi 0.2/giây.
- Khi cạn stamina, sói trở về tốc độ thường, mồi có cơ hội thoát.

**Vị trí code:** `HunterStrategy.act()`, `Carnivore.move()`.

### 7.5. Tìm hồ nước gần nhất

**Mục đích:** `ThirstyStrategy` cần biết đi về hướng nào để uống.

**Cách hoạt động:** `World.findNearestWaterTile(x, y)` quét toàn bộ grid, trả về tâm tile WATER có khoảng cách ngắn nhất tới (x, y). Hiện cài đặt O(gridW × gridH); với grid ~60x33 thì chỉ ~2000 ops/animal, chấp nhận được.

**Vị trí code:** `World.findNearestWaterTile()`.

### 7.6. Plant reproduction có giới hạn

**Mục đích:** Cỏ và Cây ăn quả tự lan, nhưng không bùng nổ ra cả map.

**Cách hoạt động:** Mỗi cây có `reproduceTimer`. Khi timer đạt `reproduceInterval` (30s mặc định, 45s cho FruitTree), quay xổ với xác suất `reproduceChance` (10% mặc định). Nếu trúng, sinh con ở vị trí cách 20-50px theo hướng ngẫu nhiên (không trên WATER/ROCK).

**Vị trí code:** `Plant.update()`, `Plant.tryReproduce()`.

---

## 8. Hướng dẫn sử dụng ngắn gọn (cho người dùng cuối)

1. **Bước 1 — Khởi động:** Mở IntelliJ, set JDK = Oracle 1.8, Run `Main.java`. Cửa sổ full màn hình hiện ra.
2. **Bước 2 — Quan sát:** 30-60 giây đầu, các loài tự sinh hoạt: thỏ ăn cỏ, sói săn thỏ, voi đi nghênh ngang, cá bơi trong hồ.
3. **Bước 3 — Can thiệp:**
   - Gieo cỏ tại nơi thiếu thức ăn: click trái.
   - Đặt vách đá làm vật cản: click phải.
   - Đổi mùa (đo phản ứng): bấm nút Season.
   - Phóng to góc nhỏ: Zoom +.
4. **Bước 4 — Thu thập số liệu:** Sau khi chạy 2-5 phút, bấm **Export CSV** → có file `stats-xxxx.csv` trong thư mục project.
5. **Bước 5 — Phân tích:** Mở CSV bằng Excel, Insert → Chart → Line Chart cho các cột population để có biểu đồ Lotka-Volterra cho báo cáo.

---

## 9. UML

Source code PlantUML ở folder `docs/uml/`:
- `class-diagram.puml` — sơ đồ lớp đầy đủ (Entity hierarchy, Strategy, World, View, Event, Stats, Main).
- `package-diagram.puml` — sơ đồ phụ thuộc giữa các nhóm logic.

**Cách export PNG cho báo cáo Word:**
1. Mở https://www.plantuml.com/plantuml/uml
2. Copy nội dung file `.puml` paste vào → bấm **Submit** → tải PNG/SVG.
3. Hoặc cài plugin **PlantUML integration** trong IntelliJ + **Graphviz** để preview tại chỗ.

Chi tiết trong `docs/uml/README.md`.

---

## 10. Phân công nhóm (tham khảo — điền theo thực tế)

| Thành viên | Module phụ trách | % đóng góp |
|---|---|---|
| ... | ... | ... |

Mỗi thành viên ghi rõ phần code đã viết, số commit, thời gian đóng góp. % nên đối soát với log Git (`git shortlog -s -n`).

---

*Hết.*
