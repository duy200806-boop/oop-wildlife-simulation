# Wild-Life Eco Simulation

Bài tập lớn OOP - mô phỏng hệ sinh thái hoang dã (Đề tài 3).

## Yêu cầu
- Java 8 (Oracle JDK, đã có sẵn JavaFX bundled).
- IntelliJ IDEA hoặc Eclipse / NetBeans.

## Cách chạy
1. Mở folder project trong IntelliJ IDEA (Open project).
2. File → Project Structure → set Project SDK = 1.8.
3. Right-click folder `src` → Mark Directory as → Sources Root.
4. Mở `src/Main.java` → bấm Run.

## Điều khiển
- **Click trái**: gieo cỏ tại vị trí click.
- **Click phải**: đặt đá (vật cản, không ai vào được).
- **Renderer: Basic/Sprite**: đổi chế độ vẽ.
- **Zoom + / Zoom - / Reset**: phóng to / thu nhỏ.
- **Export CSV**: lưu thống kê quần thể ra file (xem thư mục project).

## Các loài
| Loài | Màu (Basic) | Chiến lược |
|---|---|---|
| Cỏ | xanh đậm | (thực vật) |
| Thỏ | trắng | Scared (chạy trốn) |
| Hươu | nâu | Scared |
| Sói | đỏ đậm | Hunter (săn) |
| Hổ | cam | Hunter |
| Voi | xám | Apex (đi nghênh ngang) |

## Vùng địa hình
- Đồng cỏ (nửa trên) - tốc độ chuẩn.
- Rừng (góc dưới-trái) - tốc độ 0.8x.
- Hồ (góc dưới-phải) - không qua được.
- Bụi rậm - chỉ động vật ăn cỏ vào được.
- Bùn - tốc độ 0.4x.
- Đá - không ai vào được (do user đặt).

## Cơ chế chính
- Đói (hunger) tăng dần. Hunger > 0.7 → đổi sang AggressiveStrategy (lao vào ăn bất chấp).
- Hunger >= max → chết.
- Carnivore không vào được bụi rậm → thỏ ẩn được.
- Mỗi sự kiện (ăn, chết, tấn công) phát qua EventBus → AudioSystem + StatisticsCollector nhận.

## Sound files (chưa có)
Để có âm thanh thật, drop 3 file `.wav` vào `src/`:
- `roar.wav` - tiếng gầm khi carnivore ăn mồi.
- `eat.wav` - tiếng ăn của herbivore.
- `death.wav` - tiếng chết.

Khi chưa có file, console sẽ log `[Audio] ATTACK` để xác nhận event.
