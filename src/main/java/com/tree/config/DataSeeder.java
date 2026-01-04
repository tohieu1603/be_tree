package com.tree.config;

import com.tree.entity.Article;
import com.tree.entity.Category;
import com.tree.entity.Product;
import com.tree.entity.User;
import com.tree.repository.ArticleRepository;
import com.tree.repository.CategoryRepository;
import com.tree.repository.ProductRepository;
import com.tree.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Seed data cho website bán đồ gỗ trầm hương
 * - Categories: Vòng tay, Tượng phật, Chuỗi hạt, Nhang trầm, Tinh dầu, Phụ kiện
 * - Products: Các sản phẩm trầm hương thật
 * - Articles: Bài viết về trầm hương
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ArticleRepository articleRepository;
    private final PasswordEncoder passwordEncoder;

    // Local images - Products
    private static final String IMG_BRACELET_1 = "/uploads/products/vong-tay-tram-huong-1.jpg";
    private static final String IMG_BRACELET_2 = "/uploads/products/vong-tay-tram-huong-2.jpg";
    private static final String IMG_BRACELET_KY_NAM = "/uploads/products/vong-tay-ky-nam.jpg";
    private static final String IMG_BUDDHA_1 = "/uploads/products/tuong-phat-tram-huong-1.jpg";
    private static final String IMG_BUDDHA_2 = "/uploads/products/tuong-phat-tram-huong-2.jpg";
    private static final String IMG_BEADS = "/uploads/products/hat-tram-huong-108.jpg";
    private static final String IMG_INCENSE_1 = "/uploads/products/nhang-tram-huong.jpg";
    private static final String IMG_INCENSE_2 = "/uploads/products/nhang-vong-tram-huong.jpg";
    private static final String IMG_OIL = "/uploads/products/tinh-dau-tram-huong.jpg";
    private static final String IMG_WOOD = "/uploads/products/go-tram-huong-nguyen-khoi.jpg";
    private static final String IMG_NECKLACE = "/uploads/products/vong-co-tram-huong.jpg";
    private static final String IMG_PEN = "/uploads/products/but-tram-huong.jpg";

    // Local images - Categories
    private static final String IMG_CAT_VONG_TAY = "/uploads/categories/cat-vong-tay.jpg";
    private static final String IMG_CAT_TUONG_PHAT = "/uploads/categories/cat-tuong-phat.jpg";
    private static final String IMG_CAT_CHUOI_HAT = "/uploads/categories/cat-chuoi-hat.jpg";
    private static final String IMG_CAT_NHANG = "/uploads/categories/cat-nhang.jpg";
    private static final String IMG_CAT_TINH_DAU = "/uploads/categories/cat-tinh-dau.jpg";
    private static final String IMG_CAT_GO = "/uploads/categories/cat-go-nguyen-khoi.jpg";
    private static final String IMG_CAT_KIEN_THUC = "/uploads/categories/cat-kien-thuc.jpg";
    private static final String IMG_CAT_HUONG_DAN = "/uploads/categories/cat-huong-dan.jpg";
    private static final String IMG_CAT_TIN_TUC = "/uploads/categories/cat-tin-tuc.jpg";

    // Local images - Articles
    private static final String IMG_ART_TRAM_HUONG = "/uploads/articles/tram-huong-la-gi.jpg";
    private static final String IMG_ART_PHAN_BIET = "/uploads/articles/phan-biet-tram-huong.jpg";
    private static final String IMG_ART_CONG_DUNG = "/uploads/articles/cong-dung-tram-huong.jpg";
    private static final String IMG_ART_BAO_QUAN = "/uploads/articles/bao-quan-tram-huong.jpg";

    @Override
    public void run(String... args) {
        seedUsers();
        seedCategories();
        seedProducts();
        seedArticles();
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .email("admin@tramhuong.vn")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("Quản trị viên")
                    .role(User.Role.ADMIN)
                    .active(true)
                    .build();
            userRepository.save(admin);
            log.info("Seeded admin user: admin@tramhuong.vn / admin123");
        }
    }

    private void seedCategories() {
        if (categoryRepository.count() == 0) {
            // Product categories (sortOrder >= 10)
            categoryRepository.save(Category.builder()
                    .name("Vòng Tay Trầm Hương").slug("vong-tay-tram-huong")
                    .description("Vòng tay trầm hương thiên nhiên, mang lại may mắn và bình an")
                    .sortOrder(10).active(true).build());

            categoryRepository.save(Category.builder()
                    .name("Tượng Phật Trầm Hương").slug("tuong-phat-tram-huong")
                    .description("Tượng Phật điêu khắc từ gỗ trầm hương quý hiếm")
                    .sortOrder(11).active(true).build());

            categoryRepository.save(Category.builder()
                    .name("Chuỗi Hạt Trầm").slug("chuoi-hat-tram")
                    .description("Chuỗi hạt trầm hương 108 hạt, tràng hạt niệm Phật")
                    .sortOrder(12).active(true).build());

            categoryRepository.save(Category.builder()
                    .name("Nhang Trầm Hương").slug("nhang-tram-huong")
                    .description("Nhang trầm hương nguyên chất, không hóa chất")
                    .sortOrder(13).active(true).build());

            categoryRepository.save(Category.builder()
                    .name("Tinh Dầu Trầm").slug("tinh-dau-tram")
                    .description("Tinh dầu trầm hương thiên nhiên 100%")
                    .sortOrder(14).active(true).build());

            categoryRepository.save(Category.builder()
                    .name("Gỗ Trầm Nguyên Khối").slug("go-tram-nguyen-khoi")
                    .description("Gỗ trầm hương nguyên khối để thờ, trưng bày")
                    .sortOrder(15).active(true).build());

            // Article categories (sortOrder < 10)
            categoryRepository.save(Category.builder()
                    .name("Kiến Thức Trầm Hương").slug("kien-thuc-tram-huong")
                    .description("Bài viết chia sẻ kiến thức về trầm hương")
                    .sortOrder(1).active(true).build());

            categoryRepository.save(Category.builder()
                    .name("Phong Thủy").slug("phong-thuy")
                    .description("Bài viết về phong thủy và trầm hương")
                    .sortOrder(2).active(true).build());

            categoryRepository.save(Category.builder()
                    .name("Tin Tức").slug("tin-tuc")
                    .description("Tin tức mới nhất về thị trường trầm hương")
                    .sortOrder(3).active(true).build());

            log.info("Seeded 9 categories (6 products, 3 articles)");
        }
    }

    private void seedProducts() {
        if (productRepository.count() == 0) {
            Category vongTay = categoryRepository.findBySlug("vong-tay-tram-huong").orElse(null);
            Category tuongPhat = categoryRepository.findBySlug("tuong-phat-tram-huong").orElse(null);
            Category chuoiHat = categoryRepository.findBySlug("chuoi-hat-tram").orElse(null);
            Category nhangTram = categoryRepository.findBySlug("nhang-tram-huong").orElse(null);
            Category tinhDau = categoryRepository.findBySlug("tinh-dau-tram").orElse(null);
            Category goTram = categoryRepository.findBySlug("go-tram-nguyen-khoi").orElse(null);

            // === VÒNG TAY TRẦM HƯƠNG ===
            if (vongTay != null) {
                productRepository.save(Product.builder()
                        .name("Vòng Tay Trầm Hương Sánh Chìm 8mm")
                        .slug("vong-tay-tram-huong-sanh-chim-8mm")
                        .summary("Vòng tay trầm hương sánh chìm tự nhiên, hạt 8mm, dành cho nam và nữ")
                        .description("## Vòng Tay Trầm Hương Sánh Chìm 8mm\n\n" +
                                "Vòng tay được làm từ **trầm hương sánh chìm** nguyên chất, khai thác từ rừng tự nhiên.\n\n" +
                                "### Đặc điểm:\n" +
                                "- Hạt trầm 8mm, phù hợp cả nam và nữ\n" +
                                "- Mùi hương tự nhiên, thơm dịu nhẹ\n" +
                                "- Chìm nước 100%\n" +
                                "- Vân gỗ đẹp tự nhiên\n\n" +
                                "### Công dụng:\n" +
                                "- Mang lại may mắn, bình an\n" +
                                "- Giúp tinh thần thư thái\n" +
                                "- Hỗ trợ thiền định\n" +
                                "- Tăng cường năng lượng tích cực")
                        .featuredImage(IMG_BRACELET_1)
                        .images(IMG_BRACELET_1 + "," + IMG_BRACELET_2 + "," + IMG_BRACELET_KY_NAM)
                        .price(new BigDecimal("2500000"))
                        .originalPrice(new BigDecimal("3000000"))
                        .sku("VT-SC-8MM-001")
                        .dimensions("8mm x 18 hạt")
                        .material("Trầm hương sánh chìm")
                        .color("Nâu đen tự nhiên")
                        .weight(0.015)
                        .stockQuantity(50)
                        .isFeatured(true)
                        .isActive(true)
                        .metaTitle("Vòng Tay Trầm Hương Sánh Chìm 8mm - Chính Hãng 100%")
                        .metaDescription("Vòng tay trầm hương sánh chìm 8mm tự nhiên, chìm nước 100%. Mang lại may mắn, bình an. Bảo hành trọn đời.")
                        .metaKeywords("vòng tay trầm hương, trầm sánh chìm, vòng tay phong thủy")
                        .viewCount(156L)
                        .category(vongTay)
                        .build());

                productRepository.save(Product.builder()
                        .name("Vòng Tay Trầm Hương Tốc 12mm")
                        .slug("vong-tay-tram-huong-toc-12mm")
                        .summary("Vòng tay trầm hương tốc hạt 12mm, dành cho nam giới")
                        .description("## Vòng Tay Trầm Hương Tốc 12mm\n\n" +
                                "Vòng tay **trầm hương tốc** cao cấp, hạt lớn 12mm phù hợp cho nam giới.\n\n" +
                                "### Đặc điểm:\n" +
                                "- Hạt trầm 12mm, size lớn cho nam\n" +
                                "- Trầm tốc - loại trầm chất lượng cao\n" +
                                "- Mùi hương đậm đặc, bền lâu\n" +
                                "- Vân tốc đẹp, rõ nét")
                        .featuredImage(IMG_BRACELET_2)
                        .images(IMG_BRACELET_2 + "," + IMG_BRACELET_1)
                        .price(new BigDecimal("4500000"))
                        .originalPrice(new BigDecimal("5500000"))
                        .sku("VT-TOC-12MM-001")
                        .dimensions("12mm x 17 hạt")
                        .material("Trầm hương tốc")
                        .color("Nâu vàng")
                        .weight(0.025)
                        .stockQuantity(30)
                        .isFeatured(true)
                        .isActive(true)
                        .metaTitle("Vòng Tay Trầm Hương Tốc 12mm - Hàng Cao Cấp")
                        .metaDescription("Vòng tay trầm hương tốc 12mm cao cấp dành cho nam. Mùi hương đậm đặc, vân tốc đẹp. Bảo hành trọn đời.")
                        .metaKeywords("vòng tay trầm tốc, trầm hương nam, vòng tay 12mm")
                        .viewCount(89L)
                        .category(vongTay)
                        .build());

                productRepository.save(Product.builder()
                        .name("Vòng Tay Trầm Hương Nữ 6mm")
                        .slug("vong-tay-tram-huong-nu-6mm")
                        .summary("Vòng tay trầm hương hạt nhỏ 6mm, thiết kế tinh tế cho nữ")
                        .description("## Vòng Tay Trầm Hương Nữ 6mm\n\n" +
                                "Vòng tay trầm hương thiết kế đặc biệt cho phái nữ với hạt nhỏ 6mm tinh tế.")
                        .featuredImage(IMG_BRACELET_KY_NAM)
                        .images(IMG_BRACELET_KY_NAM + "," + IMG_BRACELET_1)
                        .price(new BigDecimal("1800000"))
                        .originalPrice(new BigDecimal("2200000"))
                        .sku("VT-NU-6MM-001")
                        .dimensions("6mm x 20 hạt")
                        .material("Trầm hương thiên nhiên")
                        .color("Nâu nhạt")
                        .weight(0.010)
                        .stockQuantity(45)
                        .isFeatured(false)
                        .isActive(true)
                        .viewCount(67L)
                        .category(vongTay)
                        .build());
            }

            // === TƯỢNG PHẬT TRẦM HƯƠNG ===
            if (tuongPhat != null) {
                productRepository.save(Product.builder()
                        .name("Tượng Phật Di Lặc Trầm Hương")
                        .slug("tuong-phat-di-lac-tram-huong")
                        .summary("Tượng Phật Di Lặc điêu khắc thủ công từ gỗ trầm hương nguyên khối")
                        .description("## Tượng Phật Di Lặc Trầm Hương\n\n" +
                                "Tượng Phật Di Lặc được **điêu khắc thủ công** bởi nghệ nhân lành nghề từ gỗ trầm hương nguyên khối.\n\n" +
                                "### Đặc điểm:\n" +
                                "- Gỗ trầm hương nguyên khối\n" +
                                "- Điêu khắc thủ công tinh xảo\n" +
                                "- Mùi hương trầm tự nhiên\n" +
                                "- Tác phẩm độc bản\n\n" +
                                "### Ý nghĩa phong thủy:\n" +
                                "- Mang lại niềm vui, hạnh phúc\n" +
                                "- Thu hút tài lộc\n" +
                                "- Xua đuổi tà khí")
                        .featuredImage(IMG_BUDDHA_1)
                        .images(IMG_BUDDHA_1 + "," + IMG_BUDDHA_2)
                        .price(new BigDecimal("15000000"))
                        .originalPrice(new BigDecimal("18000000"))
                        .sku("TP-DL-001")
                        .dimensions("Cao 25cm x Rộng 15cm")
                        .material("Trầm hương nguyên khối")
                        .color("Nâu đen tự nhiên")
                        .weight(1.2)
                        .stockQuantity(5)
                        .isFeatured(true)
                        .isActive(true)
                        .metaTitle("Tượng Phật Di Lặc Trầm Hương - Điêu Khắc Thủ Công")
                        .metaDescription("Tượng Phật Di Lặc trầm hương nguyên khối, điêu khắc thủ công. Mang lại may mắn, tài lộc. Bảo hành trọn đời.")
                        .metaKeywords("tượng phật di lặc, trầm hương, tượng gỗ trầm")
                        .viewCount(234L)
                        .category(tuongPhat)
                        .build());

                productRepository.save(Product.builder()
                        .name("Tượng Quan Âm Bồ Tát Trầm Hương")
                        .slug("tuong-quan-am-bo-tat-tram-huong")
                        .summary("Tượng Quan Âm Bồ Tát điêu khắc từ trầm hương, mang lại bình an")
                        .description("## Tượng Quan Âm Bồ Tát Trầm Hương\n\n" +
                                "Tượng Quan Âm Bồ Tát được chế tác từ gỗ trầm hương quý hiếm.")
                        .featuredImage(IMG_BUDDHA_2)
                        .images(IMG_BUDDHA_2 + "," + IMG_BUDDHA_1)
                        .price(new BigDecimal("22000000"))
                        .originalPrice(new BigDecimal("25000000"))
                        .sku("TP-QA-001")
                        .dimensions("Cao 35cm x Rộng 12cm")
                        .material("Trầm hương nguyên khối")
                        .color("Nâu đậm")
                        .weight(1.8)
                        .stockQuantity(3)
                        .isFeatured(true)
                        .isActive(true)
                        .viewCount(189L)
                        .category(tuongPhat)
                        .build());
            }

            // === CHUỖI HẠT TRẦM ===
            if (chuoiHat != null) {
                productRepository.save(Product.builder()
                        .name("Chuỗi Hạt Trầm 108 Hạt 8mm")
                        .slug("chuoi-hat-tram-108-hat-8mm")
                        .summary("Chuỗi tràng hạt trầm hương 108 hạt để niệm Phật, thiền định")
                        .description("## Chuỗi Hạt Trầm 108 Hạt\n\n" +
                                "Chuỗi tràng hạt **108 hạt** trầm hương dùng để niệm Phật, thiền định.\n\n" +
                                "### Đặc điểm:\n" +
                                "- 108 hạt trầm hương thiên nhiên\n" +
                                "- Kích thước hạt 8mm\n" +
                                "- Có thể đeo như vòng tay 3-4 vòng\n" +
                                "- Mùi hương nhẹ nhàng")
                        .featuredImage(IMG_BEADS)
                        .images(IMG_BEADS)
                        .price(new BigDecimal("3500000"))
                        .originalPrice(new BigDecimal("4200000"))
                        .sku("CH-108-8MM-001")
                        .dimensions("8mm x 108 hạt")
                        .material("Trầm hương thiên nhiên")
                        .color("Nâu")
                        .weight(0.08)
                        .stockQuantity(25)
                        .isFeatured(false)
                        .isActive(true)
                        .viewCount(145L)
                        .category(chuoiHat)
                        .build());
            }

            // === NHANG TRẦM HƯƠNG ===
            if (nhangTram != null) {
                productRepository.save(Product.builder()
                        .name("Nhang Trầm Hương Không Tăm")
                        .slug("nhang-tram-huong-khong-tam")
                        .summary("Nhang trầm hương nguyên chất 100%, không tăm, không hóa chất")
                        .description("## Nhang Trầm Hương Không Tăm\n\n" +
                                "Nhang trầm hương **nguyên chất 100%**, không tăm tre, không hóa chất độc hại.\n\n" +
                                "### Đặc điểm:\n" +
                                "- 100% bột trầm hương nguyên chất\n" +
                                "- Không tăm tre - cháy hoàn toàn\n" +
                                "- Không hóa chất, an toàn\n" +
                                "- Mùi hương tự nhiên, thanh khiết")
                        .featuredImage(IMG_INCENSE_1)
                        .images(IMG_INCENSE_1 + "," + IMG_INCENSE_2)
                        .price(new BigDecimal("350000"))
                        .originalPrice(new BigDecimal("420000"))
                        .sku("NT-KT-001")
                        .dimensions("Hộp 100 que")
                        .material("Bột trầm hương nguyên chất")
                        .color("Nâu đen")
                        .weight(0.15)
                        .stockQuantity(200)
                        .isFeatured(false)
                        .isActive(true)
                        .viewCount(312L)
                        .category(nhangTram)
                        .build());

                productRepository.save(Product.builder()
                        .name("Nhang Nụ Trầm Hương")
                        .slug("nhang-nu-tram-huong")
                        .summary("Nhang nụ trầm hương thơm lâu, dùng cho bát hương")
                        .description("## Nhang Nụ Trầm Hương\n\n" +
                                "Nhang nụ trầm hương cao cấp, thời gian cháy lâu, phù hợp dùng cho bát hương.")
                        .featuredImage(IMG_INCENSE_2)
                        .images(IMG_INCENSE_2 + "," + IMG_INCENSE_1)
                        .price(new BigDecimal("280000"))
                        .originalPrice(new BigDecimal("350000"))
                        .sku("NT-NU-001")
                        .dimensions("Hộp 50 nụ")
                        .material("Bột trầm hương ép")
                        .color("Nâu")
                        .weight(0.2)
                        .stockQuantity(150)
                        .isFeatured(false)
                        .isActive(true)
                        .viewCount(198L)
                        .category(nhangTram)
                        .build());
            }

            // === TINH DẦU TRẦM ===
            if (tinhDau != null) {
                productRepository.save(Product.builder()
                        .name("Tinh Dầu Trầm Hương Nguyên Chất 10ml")
                        .slug("tinh-dau-tram-huong-nguyen-chat-10ml")
                        .summary("Tinh dầu trầm hương thiên nhiên 100%, chiết xuất bằng phương pháp cất tinh")
                        .description("## Tinh Dầu Trầm Hương Nguyên Chất\n\n" +
                                "Tinh dầu trầm hương **100% thiên nhiên**, chiết xuất bằng phương pháp cất tinh truyền thống.\n\n" +
                                "### Công dụng:\n" +
                                "- Xông phòng, khử mùi\n" +
                                "- Thư giãn, giảm stress\n" +
                                "- Hỗ trợ giấc ngủ\n" +
                                "- Thiền định")
                        .featuredImage(IMG_OIL)
                        .images(IMG_OIL)
                        .price(new BigDecimal("1200000"))
                        .originalPrice(new BigDecimal("1500000"))
                        .sku("TD-NC-10ML-001")
                        .dimensions("10ml")
                        .material("Tinh dầu trầm hương 100%")
                        .color("Vàng nhạt")
                        .weight(0.05)
                        .stockQuantity(80)
                        .isFeatured(true)
                        .isActive(true)
                        .metaTitle("Tinh Dầu Trầm Hương Nguyên Chất 10ml - 100% Thiên Nhiên")
                        .metaDescription("Tinh dầu trầm hương nguyên chất 100% thiên nhiên. Xông phòng, thư giãn, hỗ trợ giấc ngủ. Cam kết chính hãng.")
                        .metaKeywords("tinh dầu trầm hương, tinh dầu thiên nhiên, xông phòng")
                        .viewCount(267L)
                        .category(tinhDau)
                        .build());
            }

            // === GỖ TRẦM NGUYÊN KHỐI ===
            if (goTram != null) {
                productRepository.save(Product.builder()
                        .name("Gỗ Trầm Hương Nguyên Khối Để Bàn")
                        .slug("go-tram-huong-nguyen-khoi-de-ban")
                        .summary("Khúc gỗ trầm hương nguyên khối tự nhiên, để bàn làm việc hoặc thờ cúng")
                        .description("## Gỗ Trầm Hương Nguyên Khối\n\n" +
                                "Khúc gỗ trầm hương **nguyên khối** tự nhiên, giữ nguyên hình dáng gốc.\n\n" +
                                "### Đặc điểm:\n" +
                                "- Gỗ trầm nguyên khối 100%\n" +
                                "- Hình dáng tự nhiên độc đáo\n" +
                                "- Mùi hương đậm đặc\n" +
                                "- Phù hợp để bàn hoặc thờ")
                        .featuredImage(IMG_WOOD)
                        .images(IMG_WOOD)
                        .price(new BigDecimal("8500000"))
                        .originalPrice(new BigDecimal("10000000"))
                        .sku("GT-NK-001")
                        .dimensions("Khoảng 15-20cm")
                        .material("Gỗ trầm hương nguyên khối")
                        .color("Nâu đen")
                        .weight(0.8)
                        .stockQuantity(10)
                        .isFeatured(false)
                        .isActive(true)
                        .viewCount(178L)
                        .category(goTram)
                        .build());
            }

            log.info("Seeded 12 products");
        }
    }

    private void seedArticles() {
        if (articleRepository.count() == 0) {
            User admin = userRepository.findByEmail("admin@tramhuong.vn").orElse(null);
            Category kienThuc = categoryRepository.findBySlug("kien-thuc-tram-huong").orElse(null);
            Category phongThuy = categoryRepository.findBySlug("phong-thuy").orElse(null);
            Category tinTuc = categoryRepository.findBySlug("tin-tuc").orElse(null);

            if (admin != null && kienThuc != null) {
                articleRepository.save(Article.builder()
                        .title("Cách Phân Biệt Trầm Hương Thật Và Giả")
                        .slug("cach-phan-biet-tram-huong-that-va-gia")
                        .summary("Hướng dẫn chi tiết cách nhận biết trầm hương thật và các sản phẩm trầm hương giả trên thị trường")
                        .content("# Cách Phân Biệt Trầm Hương Thật Và Giả\n\n" +
                                "Trầm hương là một trong những loại gỗ quý hiếm nhất thế giới. Tuy nhiên, do giá trị cao nên thị trường xuất hiện rất nhiều sản phẩm trầm hương giả.\n\n" +
                                "## 1. Kiểm tra bằng nước\n\n" +
                                "**Trầm thật**: Trầm hương chất lượng cao sẽ chìm trong nước do có nhiều dầu.\n\n" +
                                "**Trầm giả**: Thường nổi hoặc chìm một nửa.\n\n" +
                                "## 2. Kiểm tra mùi hương\n\n" +
                                "- Trầm thật có mùi hương tự nhiên, nhẹ nhàng\n" +
                                "- Khi đốt, mùi hương thanh khiết, không gắt\n" +
                                "- Trầm giả thường có mùi hắc, khó chịu\n\n" +
                                "## 3. Quan sát vân gỗ\n\n" +
                                "Trầm hương thật có vân gỗ tự nhiên, không đều đặn. Trầm giả thường có vân đều, nhân tạo.\n\n" +
                                "## 4. Test bằng lửa\n\n" +
                                "Đốt một mẩu nhỏ:\n" +
                                "- Trầm thật cháy chậm, khói trắng, mùi thơm\n" +
                                "- Trầm giả cháy nhanh, khói đen, mùi khét")
                        .contentHtml("<h1>Cách Phân Biệt Trầm Hương Thật Và Giả</h1>...")
                        .featuredImage(IMG_ART_PHAN_BIET)
                        .featuredImageAlt("Gỗ trầm hương thật")
                        .tags("trầm hương, phân biệt trầm thật giả, kiến thức trầm hương")
                        .readingTime(5)
                        .status(Article.Status.PUBLISHED)
                        .publishedAt(LocalDateTime.now().minusDays(10))
                        .focusKeyword("phân biệt trầm hương thật giả")
                        .metaTitle("Cách Phân Biệt Trầm Hương Thật Và Giả - Hướng Dẫn Chi Tiết")
                        .metaDescription("Hướng dẫn chi tiết cách nhận biết trầm hương thật và giả. 4 phương pháp đơn giản để kiểm tra chất lượng trầm hương.")
                        .metaKeywords("trầm hương thật, trầm hương giả, cách phân biệt trầm hương")
                        .schemaType("Article")
                        .viewCount(1250L)
                        .category(kienThuc)
                        .author(admin)
                        .build());

                articleRepository.save(Article.builder()
                        .title("Công Dụng Của Trầm Hương Đối Với Sức Khỏe")
                        .slug("cong-dung-cua-tram-huong-doi-voi-suc-khoe")
                        .summary("Khám phá những lợi ích tuyệt vời của trầm hương đối với sức khỏe thể chất và tinh thần")
                        .content("# Công Dụng Của Trầm Hương Đối Với Sức Khỏe\n\n" +
                                "Trầm hương không chỉ có giá trị về mặt tâm linh mà còn mang lại nhiều lợi ích cho sức khỏe.\n\n" +
                                "## 1. Giảm stress và lo âu\n\n" +
                                "Mùi hương trầm có tác dụng làm dịu thần kinh, giúp giảm căng thẳng và lo âu.\n\n" +
                                "## 2. Cải thiện giấc ngủ\n\n" +
                                "Xông tinh dầu trầm hương trước khi ngủ giúp thư giãn và có giấc ngủ sâu hơn.\n\n" +
                                "## 3. Hỗ trợ thiền định\n\n" +
                                "Trầm hương được sử dụng trong các buổi thiền định để tăng sự tập trung.\n\n" +
                                "## 4. Kháng khuẩn tự nhiên\n\n" +
                                "Khói trầm hương có khả năng kháng khuẩn, làm sạch không khí.")
                        .contentHtml("<h1>Công Dụng Của Trầm Hương</h1>...")
                        .featuredImage(IMG_INCENSE_1)
                        .featuredImageAlt("Nhang trầm hương")
                        .tags("trầm hương, sức khỏe, tinh dầu trầm")
                        .readingTime(4)
                        .status(Article.Status.PUBLISHED)
                        .publishedAt(LocalDateTime.now().minusDays(7))
                        .focusKeyword("công dụng trầm hương")
                        .metaTitle("Công Dụng Của Trầm Hương Đối Với Sức Khỏe")
                        .metaDescription("Khám phá những lợi ích tuyệt vời của trầm hương: giảm stress, cải thiện giấc ngủ, hỗ trợ thiền định.")
                        .viewCount(890L)
                        .category(kienThuc)
                        .author(admin)
                        .build());
            }

            if (admin != null && phongThuy != null) {
                articleRepository.save(Article.builder()
                        .title("Ý Nghĩa Phong Thủy Của Vòng Tay Trầm Hương")
                        .slug("y-nghia-phong-thuy-cua-vong-tay-tram-huong")
                        .summary("Tìm hiểu ý nghĩa phong thủy và cách đeo vòng tay trầm hương đúng cách")
                        .content("# Ý Nghĩa Phong Thủy Của Vòng Tay Trầm Hương\n\n" +
                                "Vòng tay trầm hương không chỉ là trang sức mà còn mang ý nghĩa phong thủy sâu sắc.\n\n" +
                                "## Ý nghĩa phong thủy\n\n" +
                                "- **Mang lại may mắn**: Trầm hương thu hút năng lượng tích cực\n" +
                                "- **Bảo vệ bình an**: Xua đuổi tà khí, năng lượng xấu\n" +
                                "- **Tăng cường trí tuệ**: Giúp đầu óc minh mẫn\n\n" +
                                "## Cách đeo đúng\n\n" +
                                "- Đeo tay trái để thu hút năng lượng tốt\n" +
                                "- Đeo tay phải để xua đuổi năng lượng xấu\n" +
                                "- Nên tránh để vòng tiếp xúc với nước")
                        .contentHtml("<h1>Ý Nghĩa Phong Thủy</h1>...")
                        .featuredImage(IMG_BRACELET_1)
                        .featuredImageAlt("Vòng tay trầm hương")
                        .tags("vòng tay trầm hương, phong thủy, may mắn")
                        .readingTime(3)
                        .status(Article.Status.PUBLISHED)
                        .publishedAt(LocalDateTime.now().minusDays(5))
                        .focusKeyword("vòng tay trầm hương phong thủy")
                        .metaTitle("Ý Nghĩa Phong Thủy Của Vòng Tay Trầm Hương")
                        .metaDescription("Tìm hiểu ý nghĩa phong thủy của vòng tay trầm hương và cách đeo đúng để mang lại may mắn, bình an.")
                        .viewCount(756L)
                        .category(phongThuy)
                        .author(admin)
                        .build());
            }

            if (admin != null && tinTuc != null) {
                articleRepository.save(Article.builder()
                        .title("Thị Trường Trầm Hương Việt Nam 2025")
                        .slug("thi-truong-tram-huong-viet-nam-2025")
                        .summary("Cập nhật tình hình thị trường trầm hương Việt Nam năm 2025")
                        .content("# Thị Trường Trầm Hương Việt Nam 2025\n\n" +
                                "Thị trường trầm hương Việt Nam tiếp tục phát triển mạnh mẽ trong năm 2025.\n\n" +
                                "## Xu hướng\n\n" +
                                "- Nhu cầu trầm hương cao cấp tăng 15%\n" +
                                "- Người tiêu dùng quan tâm đến chất lượng hơn giá cả\n" +
                                "- Sản phẩm có chứng nhận nguồn gốc được ưa chuộng\n\n" +
                                "## Giá cả\n\n" +
                                "- Trầm sánh chìm: 2-5 triệu/vòng tay\n" +
                                "- Trầm tốc: 4-10 triệu/vòng tay\n" +
                                "- Tượng trầm: 10-50 triệu tùy kích thước")
                        .contentHtml("<h1>Thị Trường Trầm Hương</h1>...")
                        .featuredImage(IMG_ART_BAO_QUAN)
                        .featuredImageAlt("Thị trường trầm hương")
                        .tags("thị trường trầm hương, giá trầm hương, tin tức")
                        .readingTime(4)
                        .status(Article.Status.PUBLISHED)
                        .publishedAt(LocalDateTime.now().minusDays(2))
                        .focusKeyword("thị trường trầm hương 2025")
                        .metaTitle("Thị Trường Trầm Hương Việt Nam 2025 - Cập Nhật Mới Nhất")
                        .metaDescription("Cập nhật tình hình thị trường trầm hương Việt Nam 2025: xu hướng, giá cả, và những điều cần biết.")
                        .viewCount(432L)
                        .category(tinTuc)
                        .author(admin)
                        .build());
            }

            log.info("Seeded 4 articles");
        }
    }
}
