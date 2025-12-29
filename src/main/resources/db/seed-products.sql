-- Wood Sculpture Products (Tuong go)
-- Run this to replace furniture products with wood sculptures

-- Delete existing products (be careful in production!)
DELETE FROM products WHERE slug LIKE '%-go-%' OR slug LIKE 'ban-%' OR slug LIKE 'ghe-%' OR slug LIKE 'giuong-%' OR slug LIKE 'tu-%' OR slug LIKE 'ke-%' OR slug LIKE 'sofa-%';

-- Update categories for wood sculptures
DELETE FROM categories WHERE slug IN ('ban-ghe', 'giuong-ngu', 'tu-ke', 'sofa');

INSERT INTO categories (id, name, slug, description, active, sort_order, created_at, updated_at) VALUES
('c1000000-0000-0000-0000-000000000001', 'Tuong Phat', 'tuong-phat', 'Tuong Phat go dieu khac tinh xao', true, 10, NOW(), NOW()),
('c1000000-0000-0000-0000-000000000002', 'Tuong Di Lac', 'tuong-di-lac', 'Tuong Phat Di Lac mang lai may man', true, 11, NOW(), NOW()),
('c1000000-0000-0000-0000-000000000003', 'Tuong Tam Da', 'tuong-tam-da', 'Tuong Phuc Loc Tho - Tam Da', true, 12, NOW(), NOW()),
('c1000000-0000-0000-0000-000000000004', 'Tuong Nghe Thuat', 'tuong-nghe-thuat', 'Tuong go nghe thuat trang tri', true, 13, NOW(), NOW())
ON CONFLICT (slug) DO UPDATE SET name = EXCLUDED.name, description = EXCLUDED.description;

-- Wood Sculpture Products
INSERT INTO products (id, name, slug, summary, description, featured_image, images, price, original_price, sku, dimensions, material, color, weight, stock_quantity, is_featured, is_active, meta_title, meta_description, meta_keywords, view_count, category_id, created_at, updated_at) VALUES
(gen_random_uuid(), 'Tuong Phat Quan Am Go Huong', 'tuong-phat-quan-am-go-huong', 'Tuong Phat Quan Am dieu khac tay tu go huong, cao 60cm', 'Tuong Phat Quan Am Bo Tat duoc dieu khac thu cong boi cac nghe nhan lang nghe truyen thong. Go huong da duoc lua chon ky luong, van go dep tu nhien.

Dac diem:
- Go huong da Viet Nam chinh goc
- Dieu khac thu cong 100%
- Chi tiet tinh xao, duong net mem mai
- Phu hop tho cung hoac trang tri

Kich thuoc: Cao 60cm, Rong 20cm, Sau 15cm', 'https://images.unsplash.com/photo-1609167830220-7164aa360951?w=800&q=80', 'https://images.unsplash.com/photo-1545558014-8692077e9b5c?w=800&q=80', 12500000, 15000000, 'TQA-HG-001', '60 x 20 x 15 cm', 'Go huong da', 'Nau do', 8.5, 3, true, true, 'Tuong Phat Quan Am Go Huong | Tree', 'Tuong Phat Quan Am dieu khac tu go huong da, thu cong tinh xao', 'tuong phat, quan am, go huong, tuong go', 1520, 'c1000000-0000-0000-0000-000000000001', NOW(), NOW()),

(gen_random_uuid(), 'Tuong Phat Thich Ca Mau Ni Go Trac', 'tuong-phat-thich-ca-go-trac', 'Tuong Phat Thich Ca Mau Ni toa sen, go trac nguyen khoi', 'Tuong Phat Thich Ca Mau Ni ngoi toa sen, dieu khac tu khoi go trac nguyen khoi. Tac pham the hien su trang nghiem va thanh tinh.

Dac diem:
- Go trac den nguyen khoi
- Van go tu nhien van ven
- Dieu khac thu cong, mat Phat hien tu
- De toa sen tinh xao

Kich thuoc: Cao 80cm, Rong 45cm, Sau 35cm', 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=800&q=80', 'https://images.unsplash.com/photo-1609167830220-7164aa360951?w=800&q=80', 28000000, 35000000, 'TTC-TR-001', '80 x 45 x 35 cm', 'Go trac nguyen khoi', 'Den trac', 25, 2, true, true, 'Tuong Phat Thich Ca Go Trac | Tree', 'Tuong Phat Thich Ca Mau Ni go trac nguyen khoi dieu khac thu cong', 'tuong phat, thich ca, go trac, tuong go', 2100, 'c1000000-0000-0000-0000-000000000001', NOW(), NOW()),

(gen_random_uuid(), 'Tuong Di Lac Cuoi Go Bach Huong', 'tuong-di-lac-cuoi-go-bach-huong', 'Tuong Phat Di Lac cuoi vui ve, go bach huong thom', 'Tuong Phat Di Lac bung to, nu cuoi roi ro, mang lai may man thinh vuong. Dieu khac tu go bach huong toa huong thom nhe.

Dac diem:
- Go bach huong tu nhien
- Huong thom nhe, thanh tinh
- Di Lac bung to, cuoi vui ve
- Mang lai may man, phuc loc

Kich thuoc: Cao 40cm, Rong 35cm, Sau 25cm', 'https://images.unsplash.com/photo-1545558014-8692077e9b5c?w=800&q=80', '', 8500000, 10000000, 'TDL-BH-001', '40 x 35 x 25 cm', 'Go bach huong', 'Vang nhat', 6, 5, true, true, 'Tuong Di Lac Go Bach Huong | Tree', 'Tuong Di Lac cuoi go bach huong thom, mang lai may man', 'tuong di lac, go bach huong, tuong go, phong thuy', 1850, 'c1000000-0000-0000-0000-000000000002', NOW(), NOW()),

(gen_random_uuid(), 'Tuong Di Lac Keo Bao Tien Go Huong', 'tuong-di-lac-keo-bao-tien', 'Tuong Di Lac keo bao tien, bieu tuong tai loc', 'Tuong Phat Di Lac keo bao tien, bieu tuong cua tai loc va thinh vuong. Phu hop de tai phong khach, phong lam viec.

Dac diem:
- Go huong vang cao cap
- Di Lac keo bao tien lon
- Bieu tuong tai loc, thinh vuong
- Chi tiet duc tinh xao

Kich thuoc: Cao 50cm, Rong 40cm, Sau 30cm', 'https://images.unsplash.com/photo-1609167830220-7164aa360951?w=800&q=80', '', 15000000, 18000000, 'TDL-HV-002', '50 x 40 x 30 cm', 'Go huong vang', 'Vang nau', 12, 3, true, true, 'Tuong Di Lac Keo Bao Tien | Tree', 'Tuong Di Lac keo bao tien go huong, bieu tuong tai loc', 'tuong di lac, go huong, tuong go, tai loc', 1420, 'c1000000-0000-0000-0000-000000000002', NOW(), NOW()),

(gen_random_uuid(), 'Bo Tuong Tam Da Phuc Loc Tho Go Trac', 'tuong-tam-da-phuc-loc-tho-go-trac', 'Bo 3 tuong Phuc Loc Tho go trac, qua tang y nghia', 'Bo Tam Da gom 3 vi than Phuc - Loc - Tho, bieu tuong cho hanh phuc, tai loc va truong tho. Qua tang y nghia dip Tet, tan gia.

Dac diem:
- Bo 3 tuong Phuc Loc Tho
- Go trac den cao cap
- Dieu khac dong bo, can xung
- Hop qua tang sang trong

Kich thuoc moi tuong: Cao 35cm', 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=800&q=80', '', 22000000, 28000000, 'TTD-TR-001', '35 x 15 x 12 cm (moi tuong)', 'Go trac den', 'Den trac', 9, 4, true, true, 'Bo Tuong Tam Da Phuc Loc Tho | Tree', 'Bo 3 tuong Phuc Loc Tho go trac, qua tang y nghia', 'tuong tam da, phuc loc tho, go trac, qua tang', 980, 'c1000000-0000-0000-0000-000000000003', NOW(), NOW()),

(gen_random_uuid(), 'Tuong Ong Tho Go Huong Da', 'tuong-ong-tho-go-huong-da', 'Tuong Tho Cong go huong, chuc truong tho', 'Tuong Tho Cong (Than Truong Tho) cam gay truong sinh, bieu tuong cua suc khoe va song lau. Lam tu go huong da quy.

Dac diem:
- Go huong da lau nam
- Ong Tho cam gay, om dao
- Chi tiet rau toc tinh te
- Phu hop nguoi cao tuoi

Kich thuoc: Cao 45cm, Rong 18cm, Sau 15cm', 'https://images.unsplash.com/photo-1545558014-8692077e9b5c?w=800&q=80', '', 9500000, 12000000, 'TOT-HD-001', '45 x 18 x 15 cm', 'Go huong da', 'Nau do', 5.5, 6, false, true, 'Tuong Ong Tho Go Huong Da | Tree', 'Tuong Tho Cong go huong da, chuc truong tho khoe manh', 'tuong ong tho, go huong, tuong go, truong tho', 720, 'c1000000-0000-0000-0000-000000000003', NOW(), NOW()),

(gen_random_uuid(), 'Tuong Ngua Phi Go Cam Lai', 'tuong-ngua-phi-go-cam-lai', 'Tuong ngua phi nuoc dai, bieu tuong thanh cong', 'Tuong ngua phi nuoc dai, bieu tuong cua thanh cong, thang tien. Dieu khac tu go cam lai quy hiem.

Dac diem:
- Go cam lai 100% tu nhien
- Ngua phi nuoc dai, manh me
- Bieu tuong su nghiep thang tien
- Trang tri ban lam viec, phong khach

Kich thuoc: Dai 60cm, Cao 45cm', 'https://images.unsplash.com/photo-1598928636135-d146006ff4be?w=800&q=80', '', 18500000, 22000000, 'TNG-CL-001', '60 x 25 x 45 cm', 'Go cam lai', 'Do nau', 14, 2, true, true, 'Tuong Ngua Phi Go Cam Lai | Tree', 'Tuong ngua phi go cam lai, bieu tuong thanh cong', 'tuong ngua, go cam lai, tuong go, phong thuy', 1150, 'c1000000-0000-0000-0000-000000000004', NOW(), NOW()),

(gen_random_uuid(), 'Tuong Dai Bang Go Trac Den', 'tuong-dai-bang-go-trac-den', 'Tuong dai bang tung canh, bieu tuong quyen luc', 'Tuong dai bang vươn canh bay cao, bieu tuong cua quyen luc, thanh dat va khat vong. Dieu khac tu go trac den quy hiem.

Dac diem:
- Go trac den nguyen khoi
- Dai bang tung canh hung vi
- Chi tiet long vu sac net
- De go kem theo

Kich thuoc: Dai 70cm, Cao 50cm, Sau 35cm', 'https://images.unsplash.com/photo-1557401620-67270b61ea82?w=800&q=80', '', 32000000, 40000000, 'TDB-TR-001', '70 x 35 x 50 cm', 'Go trac den', 'Den', 22, 1, true, true, 'Tuong Dai Bang Go Trac Den | Tree', 'Tuong dai bang go trac den, bieu tuong quyen luc', 'tuong dai bang, go trac, tuong go, nghe thuat', 890, 'c1000000-0000-0000-0000-000000000004', NOW(), NOW())
ON CONFLICT (slug) DO NOTHING;
