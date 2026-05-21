# Hệ thống Mô phỏng Hệ sinh thái Hoang dã (Wild-Life Eco Simulation)



---

## 🌟 Tính năng nổi bật

### 1. Mô phỏng Sinh tồn phức tạp (BioLogic)
*   **Đói & Khát (Hunger & Thirst):** Động vật tự tiêu hao năng lượng theo thời gian. Khi đạt ngưỡng nguy hiểm (`> 0.7`), chúng tự thay đổi hành vi để tìm thức ăn hoặc di chuyển đến vùng nước (`WATER`) gần nhất.
*   **Tăng tốc & Thể lực (Stamina/Sprint):** Thú ăn thịt (`Carnivore`) tự động kích hoạt trạng thái săn mồi tăng tốc (x1.7 tốc độ) khi đuổi bắt mục tiêu, đồng thời tiêu hao thanh thể lực (`Stamina`). Khi kiệt sức, chúng phải di chuyển chậm lại để hồi phục.
*   **Lách bụi rậm trốn thoát:** Thỏ (`Herbivore`) có thể đi vào bụi rậm (`BUSH`), trong khi Sói và Hổ bị chặn lại. Đây là cơ chế giúp con mồi trốn thoát khỏi kẻ đi săn.
*   **Nhường đường (Steering/Separation):** Các loài động vật nhỏ tự động dạt sang một bên hoặc đổi hướng khi va chạm với động vật lớn hơn hoặc loài đầu bảng (Voi - `ApexEntity`).

### 2. Sinh địa hình & Môi trường tự nhiên (World & Seasons)
*   **Địa hình ngẫu nhiên (Procedural Generation):** Sử dụng thuật toán **Cellular Automata** để tạo ra các mảng địa hình Đồng cỏ, Rừng rậm, Bụi gai, Hồ nước, Bùn lầy với đường viền gồ ghề, uốn lượn tự nhiên giống thực tế.
*   **Ảnh hưởng địa hình:** Tốc độ di chuyển thay đổi theo địa hình (Cỏ: 1.0x, Rừng: 0.8x, Bụi: 0.7x, Bùn: 0.4x). Đá (`ROCK`) cản trở hoàn toàn di chuyển.
*   **Chu kỳ Mùa (Seasons):** Thay đổi linh hoạt 3 trạng thái môi trường:
    *   `NORMAL` (Bình thường).
    *   `DROUGHT` (Hạn hán): Tốc độ di chuyển giảm 40%, thực vật ngừng sinh trưởng, tài nguyên khan hiếm.
    *   `BREEDING` (Sinh sản): Động vật nếu được ăn no sẽ tự động nhân bản (sinh sản) để duy trì nòi giống.

### 3. Giao diện Đồ họa & Điều khiển (ViewLogic)
*   **Hai chế độ hiển thị:**
    *   **Basic:** Vẽ các hình học cơ bản có màu sắc đặc trưng đại diện cho từng loài (dành cho máy cấu hình yếu).
    *   **Sprite (Đồ họa):** Đọc chuỗi ảnh động thực tế từ folder tài nguyên để hiển thị sinh vật di chuyển mượt mà.
*   **Tương tác thủ công:**
    *   `Click chuột trái`: Gieo mầm Cỏ tại vị trí click.
    *   `Click chuột phải`: Đặt vách đá vật cản (`ROCK`).
*   **Hỗ trợ Camera:** Phóng to (`Zoom +`), thu nhỏ (`Zoom -`), đặt lại (`Reset`) để bao quát toàn cảnh hoặc xem chi tiết từng vùng nước nhỏ.
*   **Thống kê:** Xuất dữ liệu biểu đồ quần thể ra file CSV theo thời gian thực để phân tích.

---

## 🛠️ Kiến trúc và Design Patterns đã áp dụng

Dự án tuân thủ nghiêm ngặt mô hình **MVC** nhằm tách biệt hoàn toàn **BioLogic** (Model) khỏi **ViewLogic** (JavaFX UI).

*   **Strategy Pattern (`SurvivalStrategy`):** Thiết lập các chiến lược hành vi di chuyển động vật (`PassiveStrategy`, `ScaredStrategy`, `HunterStrategy`, `ThirstyStrategy`, `AggressiveStrategy`, `ApexStrategy`). Cho phép thay đổi "bộ não" linh hoạt theo trạng thái cơ thể (ví dụ: đói/khát).
*   **Observer Pattern (`EventBus` & `EventListener`):** Truyền phát các sự kiện hệ thống (`EAT`, `ATTACK`, `DEATH`, `BIRD_CHIRP`, `LEAVES_RUSTLE`) đến hệ thống âm thanh (`AudioSystem`) và bộ thu thập số liệu (`StatisticsCollector`) mà không làm rối mã nguồn chính.
*   **Composite Pattern (`World` chứa `Terrain` và `Entity`):** Quản lý cấu trúc địa hình lưới ô vuông kết hợp danh sách các thực thể động.
*   **Template Method (`Entity.update()`):** Định nghĩa khung xử lý vòng lặp chung cho mọi thực thể, cho phép các lớp con ghi đè logic cụ thể.

---

## 📂 Danh sách các Thực thể

| Thực thể | Loài | Hiển thị (Basic) | Hành vi / Đặc điểm |
| :--- | :--- | :--- | :--- |
| **Grass** | Thực vật | Xanh lá | Tự sinh sản lan ra xung quanh |
| **FruitTree** | Thực vật | Thân gỗ, quả đỏ | Tự sinh sản lan ra xung quanh |
| **Fish** | Động vật dưới nước | Bạc | Chỉ bơi trong vùng nước (`WATER`), không đói/khát |
| **Duck** | Động vật dưới nước | Trắng, mỏ vàng | Chỉ bơi trong vùng nước (`WATER`), không đói/khát |
| **Rabbit** | Động vật ăn cỏ | Trắng | Chạy trốn khi gặp thú dữ, ăn cỏ |
| **Deer** | Động vật ăn cỏ | Nâu | Chạy trốn khi gặp thú dữ, ăn cỏ |
| **Wolf** | Động vật ăn thịt | Đỏ đậm | Tấn công động vật ăn cỏ, đuổi bắt nhanh |
| **Tiger** | Động vật ăn thịt | Cam sọc đen | Tấn công động vật ăn cỏ, đuổi bắt nhanh |
| **Elephant** | Động vật đầu bảng | Xám to | Đi lang thang tự do, không sợ kẻ thù, các loài khác phải nhường đường |

---

## 🚀 Hướng dẫn Setup và Chạy dự án



1.  Mở dự án bằng **IntelliJ IDEA** (hoặc Eclipse, NetBeans).
2.  Truy cập `File` ➔ `Project Structure` ➔ mục `Project SDK` chọn phiên bản **JDK 1.8 (Java 8)**.
3.  Click chuột phải vào thư mục `src` ➔ Chọn `Mark Directory as` ➔ `Sources Root`.
4.  Mở file [Main.java](file:///d:/oop%20project/src/Main.java) ➔ Click chuột phải chọn `Run 'Main.main()'`.

### 🎵 Cách thêm Âm thanh và Hình ảnh (Sprites)
Để hiển thị đầy đủ hình ảnh động và phát âm thanh sinh động, bạn hãy bỏ các tệp tin tài nguyên vào các thư mục tương ứng:

*   **m thanh (.wav):** Bỏ vào thư mục `src/` (hoặc `src/resources/audio/`):
    - `roar.wav` (Tiếng hổ gầm khi tấn công).
    - `eat.wav` (Tiếng nhai nhóp nhép khi ăn).
    - `death.wav` (Tiếng kêu khi động vật chết).
*   **Hình ảnh (.png):** Bỏ vào thư mục `src/resources/sprites/`:
    - File tĩnh: `rabbit.png`, `wolf.png`, `deer.png`, `tiger.png`, `elephant.png`, `fish.png`, `duck.png`, `grass.png`, `fruit_tree.png`.
    - File động (nếu có): Đổi tên theo định dạng `prefix_0.png` đến `prefix_7.png` (ví dụ: `rabbit_0.png` đến `rabbit_7.png`). Chương trình sẽ tự động dựng hoạt ảnh chuyển động.
