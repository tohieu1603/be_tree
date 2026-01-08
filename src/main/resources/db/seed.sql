-- Seed data for Tree Blog
-- Run this after the application starts and creates the tables

-- Create admin user if not exists (password: admin123 - BCrypt encoded)
INSERT INTO users (id, email, password, full_name, role, active, created_at, updated_at)
SELECT 'a0000000-0000-0000-0000-000000000001', 'admin@tree.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy.Mrq4H1cQFyVJxYv4y1rDNbMd1D6kMJO', 'Admin', 'ADMIN', true, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@tree.com');

-- Create banners
INSERT INTO banners (id, title, subtitle, button_text, button_link, image_url, sort_order, active, created_at, updated_at) VALUES
('d0000000-0000-0000-0000-000000000001', 'Tuong Go Nghe Thuat', 'Tuong go dieu khac thu cong, mang dam ban sac van hoa Viet', 'Xem San Pham', '/products', 'https://images.unsplash.com/photo-1609167830220-7164aa360951?w=1600&q=80', 1, true, NOW(), NOW()),
('d0000000-0000-0000-0000-000000000002', 'Dieu Khac Thu Cong', 'Moi tac pham la mot tac pham nghe thuat doc nhat vo nhi', 'Lien He', '/contact', 'https://images.unsplash.com/photo-1545558014-8692077e9b5c?w=1600&q=80', 2, true, NOW(), NOW()),
('d0000000-0000-0000-0000-000000000003', 'Go Quy Tu Nhien', 'Go huong, go trac, go cam lai - chat luong cao cap', 'Xem Them', '/products', 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=1600&q=80', 3, true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Create categories
INSERT INTO categories (id, name, slug, description, active, sort_order, deleted, created_at, updated_at) VALUES
('c0000000-0000-0000-0000-000000000001', 'Cong nghe', 'cong-nghe', 'Tin tuc va bai viet ve cong nghe, lap trinh, AI', true, 1, false, NOW(), NOW()),
('c0000000-0000-0000-0000-000000000002', 'Kinh doanh', 'kinh-doanh', 'Kinh nghiem kinh doanh, startup, khoi nghiep', true, 2, false, NOW(), NOW()),
('c0000000-0000-0000-0000-000000000003', 'Doi song', 'doi-song', 'Bai viet ve cuoc song, suc khoe, lam dep', true, 3, false, NOW(), NOW()),
('c0000000-0000-0000-0000-000000000004', 'Huong dan', 'huong-dan', 'Huong dan, thu thuat, tips hay', true, 4, false, NOW(), NOW())
ON CONFLICT (slug) DO NOTHING;

-- Article 1: Complete React Guide
INSERT INTO articles (id, title, slug, summary, content, content_html, featured_image, featured_image_alt, tags, reading_time, is_featured, allow_comments, status, view_count, meta_title, meta_description, meta_keywords, category_id, author_id, deleted, created_at, updated_at, published_at)
SELECT
  'b0000000-0000-0000-0000-000000000001',
  'Huong dan hoc lap trinh React tu A den Z cho nguoi moi bat dau nam 2024',
  'huong-dan-hoc-lap-trinh-react-tu-a-den-z-cho-nguoi-moi-bat-dau-nam-2024',
  'Bai viet huong dan day du nhat ve ReactJS danh cho nguoi moi bat dau. Tu cai dat moi truong, hieu ve component, state, props, hooks cho den xay dung ung dung hoan chinh.',
  '## Gioi thieu ve React

React la mot thu vien JavaScript ma nguon mo duoc phat trien boi Facebook (nay la Meta) vao nam 2013. Hien nay, React la mot trong nhung cong nghe pho bien nhat de xay dung giao dien nguoi dung (UI) cho cac ung dung web.

![React Logo](https://images.unsplash.com/photo-1633356122544-f134324a6cee?w=800&q=80)

### Tai sao React lai pho bien?

React duoc ua chuong boi nhieu ly do:

- **Component-based Architecture**: Xay dung UI bang cach ghep cac component doc lap, co the tai su dung
- **Virtual DOM**: Tang hieu suat bang cach chi cap nhat nhung phan thay doi
- **Cong dong lon**: Hang trieu developer tren toan cau, nhieu tai lieu va thu vien ho tro
- **JSX**: Cu phap ket hop JavaScript va HTML, de hieu va viet code

## Cai dat moi truong phat trien

### Yeu cau he thong

Truoc khi bat dau, ban can cai dat:

1. **Node.js** (phien ban 18 tro len) - Tai tu [nodejs.org](https://nodejs.org)
2. **npm** hoac **yarn** - Di kem voi Node.js
3. **Code Editor** - Khuyen dung Visual Studio Code

![VS Code Editor](https://images.unsplash.com/photo-1461749280684-dccba630e2f6?w=800&q=80)

### Tao du an React dau tien

Su dung Create React App hoac Vite de tao du an:

```bash
# Su dung Create React App
npx create-react-app my-first-app
cd my-first-app
npm start

# Hoac su dung Vite (nhanh hon)
npm create vite@latest my-first-app -- --template react
cd my-first-app
npm install
npm run dev
```

Sau khi chay lenh tren, truy cap `http://localhost:3000` (CRA) hoac `http://localhost:5173` (Vite) de xem ung dung.

## Component trong React

Component la don vi co ban nhat cua React. Moi component la mot khoi UI doc lap, co the nhan du lieu (props) va quan ly trang thai (state) rieng.

### Function Component

Day la cach viet component pho bien nhat hien nay:

```jsx
// components/Welcome.jsx
function Welcome({ name, age }) {
  return (
    <div className="welcome-card">
      <h1>Xin chao, {name}!</h1>
      <p>Ban {age} tuoi.</p>
    </div>
  );
}

export default Welcome;
```

### Su dung Component

```jsx
// App.jsx
import Welcome from ''./components/Welcome'';

function App() {
  return (
    <div>
      <Welcome name="Nguyen Van A" age={25} />
      <Welcome name="Tran Thi B" age={30} />
    </div>
  );
}
```

![Component Structure](https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=800&q=80)

## Props va State

### Props (Properties)

Props la cach truyen du lieu tu component cha xuong component con. Props la **read-only**, khong the thay doi trong component con.

```jsx
// Component cha
function ParentComponent() {
  const userData = {
    name: "John Doe",
    email: "john@example.com",
    avatar: "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=100"
  };

  return <UserCard user={userData} />;
}

// Component con
function UserCard({ user }) {
  return (
    <div className="user-card">
      <img src={user.avatar} alt={user.name} />
      <h3>{user.name}</h3>
      <p>{user.email}</p>
    </div>
  );
}
```

### State

State la du lieu noi bo cua component, co the thay doi theo thoi gian. Khi state thay doi, component se tu dong re-render.

```jsx
import { useState } from "react";

function Counter() {
  // Khai bao state voi useState hook
  const [count, setCount] = useState(0);
  const [name, setName] = useState("");

  const increment = () => setCount(count + 1);
  const decrement = () => setCount(count - 1);

  return (
    <div className="counter">
      <h2>Counter: {count}</h2>
      <button onClick={decrement}>-</button>
      <button onClick={increment}>+</button>

      <input
        type="text"
        value={name}
        onChange={(e) => setName(e.target.value)}
        placeholder="Nhap ten cua ban"
      />
      <p>Ten: {name}</p>
    </div>
  );
}
```

## React Hooks

Hooks la tinh nang quan trong duoc gioi thieu tu React 16.8, cho phep su dung state va cac tinh nang khac trong function component.

![React Hooks](https://images.unsplash.com/photo-1587620962725-abab7fe55159?w=800&q=80)

### useState

Quan ly state trong component:

```jsx
const [value, setValue] = useState(initialValue);
```

### useEffect

Thuc hien side effects (goi API, DOM manipulation, subscriptions):

```jsx
import { useState, useEffect } from "react";

function UserProfile({ userId }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Goi API khi component mount hoac userId thay doi
    async function fetchUser() {
      setLoading(true);
      try {
        const response = await fetch(`/api/users/${userId}`);
        const data = await response.json();
        setUser(data);
      } catch (error) {
        console.error("Error:", error);
      } finally {
        setLoading(false);
      }
    }

    fetchUser();

    // Cleanup function (chay khi unmount)
    return () => {
      console.log("Cleanup");
    };
  }, [userId]); // Dependencies array

  if (loading) return <p>Dang tai...</p>;
  if (!user) return <p>Khong tim thay user</p>;

  return (
    <div>
      <h1>{user.name}</h1>
      <p>{user.email}</p>
    </div>
  );
}
```

### useContext

Chia se du lieu giua cac component ma khong can truyen props qua nhieu cap:

```jsx
import { createContext, useContext, useState } from "react";

// Tao context
const ThemeContext = createContext();

// Provider component
function ThemeProvider({ children }) {
  const [theme, setTheme] = useState("light");

  const toggleTheme = () => {
    setTheme(theme === "light" ? "dark" : "light");
  };

  return (
    <ThemeContext.Provider value={{ theme, toggleTheme }}>
      {children}
    </ThemeContext.Provider>
  );
}

// Su dung trong component
function ThemedButton() {
  const { theme, toggleTheme } = useContext(ThemeContext);

  return (
    <button
      onClick={toggleTheme}
      style={{
        background: theme === "light" ? "#fff" : "#333",
        color: theme === "light" ? "#333" : "#fff"
      }}
    >
      Toggle Theme (Current: {theme})
    </button>
  );
}
```

## Xu ly Forms

React cung cap hai cach xu ly form: Controlled va Uncontrolled components.

```jsx
import { useState } from "react";

function ContactForm() {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    message: ""
  });
  const [errors, setErrors] = useState({});

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const validate = () => {
    const newErrors = {};
    if (!formData.name) newErrors.name = "Ten la bat buoc";
    if (!formData.email) newErrors.email = "Email la bat buoc";
    if (!formData.email.includes("@")) newErrors.email = "Email khong hop le";
    return newErrors;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const validationErrors = validate();

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    console.log("Form data:", formData);
    // Gui du lieu len server
  };

  return (
    <form onSubmit={handleSubmit}>
      <div>
        <label>Ten:</label>
        <input
          type="text"
          name="name"
          value={formData.name}
          onChange={handleChange}
        />
        {errors.name && <span className="error">{errors.name}</span>}
      </div>

      <div>
        <label>Email:</label>
        <input
          type="email"
          name="email"
          value={formData.email}
          onChange={handleChange}
        />
        {errors.email && <span className="error">{errors.email}</span>}
      </div>

      <div>
        <label>Tin nhan:</label>
        <textarea
          name="message"
          value={formData.message}
          onChange={handleChange}
        />
      </div>

      <button type="submit">Gui</button>
    </form>
  );
}
```

## React Router - Dinh tuyen

React Router giup dieu huong giua cac trang trong ung dung Single Page Application (SPA).

```jsx
import { BrowserRouter, Routes, Route, Link, useParams } from "react-router-dom";

// Layout component
function Layout({ children }) {
  return (
    <div>
      <nav>
        <Link to="/">Trang chu</Link>
        <Link to="/about">Gioi thieu</Link>
        <Link to="/products">San pham</Link>
      </nav>
      <main>{children}</main>
    </div>
  );
}

// Pages
function Home() {
  return <h1>Trang chu</h1>;
}

function About() {
  return <h1>Gioi thieu</h1>;
}

function ProductDetail() {
  const { id } = useParams();
  return <h1>Chi tiet san pham: {id}</h1>;
}

// App
function App() {
  return (
    <BrowserRouter>
      <Layout>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/about" element={<About />} />
          <Route path="/products/:id" element={<ProductDetail />} />
        </Routes>
      </Layout>
    </BrowserRouter>
  );
}
```

![Web Development](https://images.unsplash.com/photo-1498050108023-c5249f4df085?w=800&q=80)

## Best Practices

### 1. Cau truc thu muc

```
src/
├── components/
│   ├── common/
│   │   ├── Button.jsx
│   │   └── Input.jsx
│   └── layout/
│       ├── Header.jsx
│       └── Footer.jsx
├── pages/
│   ├── Home.jsx
│   └── About.jsx
├── hooks/
│   └── useAuth.js
├── context/
│   └── AuthContext.jsx
├── services/
│   └── api.js
└── utils/
    └── helpers.js
```

### 2. Naming Conventions

- **Components**: PascalCase (UserCard, ProductList)
- **Files**: kebab-case hoac PascalCase
- **Functions/Variables**: camelCase
- **Constants**: UPPER_SNAKE_CASE

### 3. Performance Tips

- Su dung `React.memo()` cho components khong can re-render thuong xuyen
- Su dung `useMemo()` va `useCallback()` de memoize values va functions
- Lazy loading components voi `React.lazy()` va `Suspense`
- Tranh tao object/array moi trong render

## Ket luan

React la mot cong cu manh me va linh hoat de xay dung giao dien nguoi dung hien dai. Voi kien thuc co ban tren, ban da san sang bat dau hanh trinh hoc React cua minh.

**Buoc tiep theo:**
- Thuc hanh xay dung cac du an nho
- Tim hieu them ve Redux hoac Zustand cho state management
- Hoc Next.js de xay dung ung dung full-stack
- Tham gia cong dong React de hoc hoi va chia se

Chuc ban thanh cong tren con duong tro thanh React Developer!',
  '',
  'https://images.unsplash.com/photo-1633356122544-f134324a6cee?w=1200&q=80',
  'Huong dan lap trinh React cho nguoi moi bat dau',
  'react,javascript,frontend,web development,hooks,component',
  25, true, true, 'PUBLISHED', 15230,
  'Huong dan hoc React tu A-Z cho nguoi moi 2024 | Tree Blog',
  'Bai huong dan day du ve ReactJS cho nguoi moi bat dau. Hoc ve component, props, state, hooks, routing va best practices.',
  'react, reactjs, javascript, frontend, web development, hooks, component',
  'c0000000-0000-0000-0000-000000000001',
  (SELECT id FROM users WHERE email = 'admin@tree.com' LIMIT 1),
  false,
  NOW() - INTERVAL '5 days', NOW() - INTERVAL '5 days', NOW() - INTERVAL '5 days'
WHERE NOT EXISTS (SELECT 1 FROM articles WHERE slug = 'huong-dan-hoc-lap-trinh-react-tu-a-den-z-cho-nguoi-moi-bat-dau-nam-2024');

-- Article 2: AI Guide
INSERT INTO articles (id, title, slug, summary, content, content_html, featured_image, featured_image_alt, tags, reading_time, is_featured, allow_comments, status, view_count, meta_title, meta_description, meta_keywords, category_id, author_id, deleted, created_at, updated_at, published_at)
SELECT
  'b0000000-0000-0000-0000-000000000002',
  'Tri tue nhan tao AI la gi? Tong quan ve AI va ung dung trong cuoc song',
  'tri-tue-nhan-tao-ai-la-gi-tong-quan-ve-ai-va-ung-dung-trong-cuoc-song',
  'Tim hieu ve tri tue nhan tao (AI), cac loai AI, lich su phat trien, ung dung thuc te va tuong lai cua AI trong moi linh vuc cuoc song.',
  '## Tri tue nhan tao (AI) la gi?

Tri tue nhan tao (Artificial Intelligence - AI) la mot nhanh cua khoa hoc may tinh tap trung vao viec tao ra cac he thong may tinh co kha nang thuc hien nhung nhiem vu thong thuong doi hoi tri thong minh cua con nguoi.

![AI Technology](https://images.unsplash.com/photo-1677442136019-21780ecad995?w=800&q=80)

Cac nhiem vu nay bao gom:
- **Hoc tap** (Learning): Kha nang thu thap thong tin va hieu cach su dung
- **Suy luan** (Reasoning): Dua ra ket luan tu thong tin da co
- **Tu sua loi** (Self-correction): Tu dong cai thien qua thoi gian
- **Sang tao** (Creativity): Tao ra noi dung, y tuong moi

## Lich su phat trien cua AI

### Nhung nam 1950s - Khoi nguon

- **1950**: Alan Turing cong bo bai bao "Computing Machinery and Intelligence" va de xuat Turing Test
- **1956**: Hoi nghi Dartmouth - noi khai sinh thuat ngu "Artificial Intelligence"
- **1958**: John McCarthy phat trien ngon ngu LISP

![Alan Turing](https://images.unsplash.com/photo-1620712943543-bcc4688e7485?w=800&q=80)

### Nhung nam 1960s-1970s - Ky vong lon

- Cac chuong trinh AI dau tien ra doi
- He thong chuyen gia (Expert Systems)
- ELIZA - chatbot dau tien

### Nhung nam 1980s-1990s - AI Winter

- Ky vong khong duoc dap ung
- Cat giam tai tro nghien cuu
- Nhung tien bo nho trong machine learning

### Nhung nam 2000s - AI Renaissance

- Big Data bung no
- GPU manh me hon
- Cloud Computing phat trien
- Deep Learning dot pha

### 2020s - AI Everywhere

- ChatGPT, GPT-4, Claude
- Text-to-Image (DALL-E, Midjourney)
- AI trong moi linh vuc

## Cac loai AI

### 1. Narrow AI (AI hep)

Duoc thiet ke de thuc hien mot nhiem vu cu the. Day la loai AI pho bien nhat hien nay.

**Vi du:**
- Siri, Google Assistant, Alexa
- Face Recognition
- De xuat san pham tren Amazon, Netflix
- Spam filters trong email

### 2. General AI (AGI - AI tong quat)

AI co kha nang hoc va thuc hien bat ky nhiem vu tri tue nao nhu con nguoi. **Chua dat duoc.**

### 3. Super AI (ASI)

AI vuot xa tri thong minh con nguoi trong moi linh vuc. **Con la ly thuyet.**

## Machine Learning vs Deep Learning

![Machine Learning](https://images.unsplash.com/photo-1555949963-aa79dcee981c?w=800&q=80)

### Machine Learning

May hoc tu du lieu ma khong can lap trinh cu the:

- **Supervised Learning**: Hoc tu du lieu da gan nhan
- **Unsupervised Learning**: Tim pattern trong du lieu chua gan nhan
- **Reinforcement Learning**: Hoc tu phan hoi (reward/penalty)

### Deep Learning

La mot nhanh cua Machine Learning su dung mang neural nhieu lop:

- **CNN (Convolutional Neural Networks)**: Xu ly hinh anh
- **RNN (Recurrent Neural Networks)**: Xu ly chuoi, van ban
- **Transformers**: Mo hinh hien dai (GPT, BERT)

## Ung dung AI trong cuoc song

### 1. Y te va suc khoe

- Chan doan benh tu hinh anh X-quang, MRI
- Phat trien thuoc moi nhanh hon
- Theo doi suc khoe qua thiet bi deo
- Phau thuat robot

### 2. Giao duc

- Hoc tap ca nhan hoa
- Tu dong cham diem bai thi
- Tro ly ao ho tro hoc sinh
- Dich thuat tu dong

### 3. Tai chinh va ngan hang

- Phat hien gian lan
- Du bao thi truong chung khoan
- Chatbot ho tro khach hang
- Danh gia tin dung

![Finance AI](https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=800&q=80)

### 4. Giao thong van tai

- Xe tu lai (Tesla, Waymo)
- Toi uu hoa giao thong
- Giao hang bang drone
- Logistics thong minh

### 5. Giai tri va truyen thong

- De xuat noi dung (Netflix, Spotify, YouTube)
- Game AI thong minh
- Tao noi dung tu dong
- Chinh sua anh/video

## AI Tools pho bien hien nay

### Text AI
- **ChatGPT, GPT-4** (OpenAI)
- **Claude** (Anthropic)
- **Gemini** (Google)
- **Copilot** (Microsoft)

### Image AI
- **DALL-E 3** (OpenAI)
- **Midjourney**
- **Stable Diffusion**
- **Adobe Firefly**

### Video AI
- **Sora** (OpenAI)
- **Runway**
- **Pika**

### Code AI
- **GitHub Copilot**
- **Cursor**
- **Tabnine**

## Thach thuc va van de dao duc

### 1. Mat viec lam

AI co the thay the nhieu cong viec truyen thong, can dao tao lai nguoi lao dong.

### 2. Thien kien (Bias)

AI co the hoc va khuech dai cac thien kien tu du lieu dao tao.

### 3. Quyen rieng tu

Thu thap va su dung du lieu ca nhan de dao tao AI.

### 4. An ninh

AI co the bi su dung cho muc dich xau (deepfake, tan cong mang).

### 5. Kiem soat

Ai chiu trach nhiem khi AI gay ra thiet hai?

## Tuong lai cua AI

### Xu huong trong 5-10 nam toi

1. **AI tro thanh commodity** - De tiep can, re hon
2. **Multimodal AI** - Xu ly nhieu loai du lieu cung luc
3. **AI Agents** - AI tu dong thuc hien cong viec phuc tap
4. **Edge AI** - AI chay tren thiet bi ca nhan
5. **Quy dinh phap ly** - Luat ve AI duoc hoan thien

![Future AI](https://images.unsplash.com/photo-1485827404703-89b55fcc595e?w=800&q=80)

## Lam the nao de bat dau voi AI?

### Nguoi dung thuong

- Su dung cac cong cu AI trong cong viec hang ngay
- Hoc cach viet prompt hieu qua
- Theo doi tin tuc ve AI

### Lap trinh vien

- Hoc Python va cac thu vien ML (TensorFlow, PyTorch)
- Tim hieu ve APIs cua OpenAI, Anthropic
- Thuc hanh xay dung ung dung AI don gian

### Doanh nghiep

- Xac dinh bai toan co the ap dung AI
- Bat dau voi cac giai phap AI san co
- Xay dung doi ngu co hieu biet ve AI

## Ket luan

AI dang va se tiep tuc thay doi moi khia canh cua cuoc song. Thay vi lo so, chung ta nen:

- **Hoc hoi** va tim hieu ve AI
- **Ap dung** AI de tang hieu qua cong viec
- **Theo doi** cac van de dao duc va an toan
- **Chuan bi** cho nhung thay doi trong tuong lai

AI la cong cu - va nhu moi cong cu khac, gia tri cua no phu thuoc vao cach chung ta su dung no.

---

*Ban da san sang cho ky nguyen AI chua?*',
  '',
  'https://images.unsplash.com/photo-1677442136019-21780ecad995?w=1200&q=80',
  'Tri tue nhan tao AI va cac ung dung',
  'AI,machine learning,deep learning,cong nghe,tuong lai',
  30, true, true, 'PUBLISHED', 28450,
  'Tri tue nhan tao AI la gi? Tong quan A-Z ve AI | Tree Blog',
  'Tim hieu day du ve AI - tri tue nhan tao: dinh nghia, lich su, cac loai AI, ung dung thuc te va tuong lai cua AI trong cuoc song.',
  'AI, artificial intelligence, machine learning, deep learning, chatgpt',
  'c0000000-0000-0000-0000-000000000001',
  (SELECT id FROM users WHERE email = 'admin@tree.com' LIMIT 1),
  false,
  NOW() - INTERVAL '3 days', NOW() - INTERVAL '3 days', NOW() - INTERVAL '3 days'
WHERE NOT EXISTS (SELECT 1 FROM articles WHERE slug = 'tri-tue-nhan-tao-ai-la-gi-tong-quan-ve-ai-va-ung-dung-trong-cuoc-song');

-- Article 3: Startup Guide
INSERT INTO articles (id, title, slug, summary, content, content_html, featured_image, featured_image_alt, tags, reading_time, is_featured, allow_comments, status, view_count, meta_title, meta_description, meta_keywords, category_id, author_id, deleted, created_at, updated_at, published_at)
SELECT
  'b0000000-0000-0000-0000-000000000003',
  '15 buoc khoi nghiep thanh cong tu con so 0 danh cho nguoi moi bat dau',
  '15-buoc-khoi-nghiep-thanh-cong-tu-con-so-0-danh-cho-nguoi-moi-bat-dau',
  'Huong dan chi tiet cach khoi nghiep tu con so 0 voi 15 buoc cu the, tu tim y tuong, lap ke hoach, xay dung san pham den marketing va mo rong quy mo.',
  '## Gioi thieu

Khoi nghiep la giac mo cua nhieu nguoi, nhung khong phai ai cung biet bat dau tu dau. Bai viet nay se huong dan ban 15 buoc cu the de khoi nghiep thanh cong, du ban dang o vi tri nao.

![Startup Journey](https://images.unsplash.com/photo-1559136555-9303baea8ebd?w=800&q=80)

## Buoc 1: Tim kiem va xac dinh y tuong kinh doanh

### Phuong phap tim y tuong

1. **Giai quyet van de ban gap** - Hay nhin vao cuoc song hang ngay
2. **Cai tien san pham/dich vu hien co** - Lam tot hon nhung gi da co
3. **Ket hop cac y tuong** - Ghep cac y tuong thanh mot thu moi
4. **Theo doi xu huong** - Cong nghe moi, thay doi xa hoi

### Danh gia y tuong

Mot y tuong tot can:
- Giai quyet van de thuc su cua khach hang
- Co thi truong du lon
- Phu hop voi kha nang cua ban
- Co the tao loi nhuan

## Buoc 2: Nghien cuu thi truong

### Phan tich khach hang muc tieu

- **Demographics**: Tuoi, gioi tinh, thu nhap, vi tri
- **Psychographics**: So thich, gia tri, loi song
- **Pain points**: Van de ho dang gap
- **Buying behavior**: Cach ho mua sam

### Phan tich doi thu canh tranh

| Yeu to | Doi thu A | Doi thu B | Ban |
|--------|-----------|-----------|-----|
| Gia | $$$ | $$ | ? |
| Chat luong | Cao | Trung binh | ? |
| Dich vu KH | Tot | Kha | ? |

![Market Research](https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=800&q=80)

## Buoc 3: Xay dung ke hoach kinh doanh

### Cau truc ke hoach kinh doanh

1. **Executive Summary** - Tom tat 1-2 trang
2. **Mo ta cong ty** - Vision, mission, gia tri
3. **Phan tich thi truong** - Quy mo, xu huong
4. **San pham/Dich vu** - Mo ta chi tiet
5. **Ke hoach Marketing** - Chien luoc tiep can khach hang
6. **Ke hoach Van hanh** - Quy trinh hoat dong
7. **Ke hoach Tai chinh** - Du toan doanh thu, chi phi
8. **Doi ngu** - Nhan su then chot

## Buoc 4: Xac dinh mo hinh kinh doanh

### Cac mo hinh pho bien

- **B2C**: Ban hang truc tiep cho nguoi tieu dung
- **B2B**: Cung cap dich vu cho doanh nghiep
- **Subscription**: Thu phi dinh ky
- **Freemium**: Co ban mien phi, tinh phi tinh nang cao cap
- **Marketplace**: Nen tang ket noi nguoi mua va nguoi ban
- **SaaS**: Phan mem dang dich vu

## Buoc 5: Lap ke hoach tai chinh

### Chi phi khoi nghiep can tinh

```
Chi phi ban dau:
- Dang ky doanh nghiep: 2-5 trieu
- Thiet bi, may moc: 10-50 trieu
- Marketing ban dau: 5-20 trieu
- Von luu dong (3-6 thang): 50-100 trieu

Tong: 70-175 trieu dong
```

### Nguon von

1. **Von tu co** - Tiet kiem ca nhan
2. **FFF** - Friends, Family, Fools
3. **Angel Investors** - Nha dau tu thien than
4. **Vay ngan hang** - Can tai san dam bao
5. **Crowdfunding** - Goi von cong dong

![Finance Planning](https://images.unsplash.com/photo-1554224155-6726b3ff858f?w=800&q=80)

## Buoc 6: Dang ky kinh doanh

### Cac loai hinh doanh nghiep

| Loai hinh | Uu diem | Nhuoc diem |
|-----------|---------|------------|
| Ho kinh doanh | Don gian | Trach nhiem vo han |
| Cong ty TNHH | Linh hoat | Phuc tap hon |
| Cong ty co phan | Huy dong von de | Quan tri phuc tap |

### Ho so can chuan bi

- CMND/CCCD cua nguoi dai dien
- Dia chi tru so
- Nganh nghe dang ky
- Von dieu le

## Buoc 7: Xay dung san pham/dich vu

### MVP (Minimum Viable Product)

San pham toi gian voi cac tinh nang co ban nhat:

1. **Xac dinh core features** - Tinh nang khong the thieu
2. **Xay dung nhanh** - Khong can hoan hao
3. **Thu nghiem voi khach hang thuc** - Lay feedback
4. **Cai tien lien tuc** - Dua tren phan hoi

### Quy trinh phat trien

```
Y tuong → Thiet ke → Xay dung → Test → Ra mat → Cai tien
    ↑___________________________________|
```

## Buoc 8: Xay dung thuong hieu

### Cac yeu to thuong hieu

- **Ten thuong hieu**: De nho, de phat am
- **Logo**: Don gian, de nhan dien
- **Slogan**: Truyen tai gia tri cot loi
- **Brand voice**: Giong dieu giao tiep

![Branding](https://images.unsplash.com/photo-1493612276216-ee3925520721?w=800&q=80)

## Buoc 9: Marketing va tiep can khach hang

### Digital Marketing

1. **SEO** - Toi uu hoa tim kiem
2. **Content Marketing** - Tao noi dung gia tri
3. **Social Media** - Facebook, Instagram, TikTok
4. **Email Marketing** - Xay dung danh sach email
5. **Paid Ads** - Quang cao tra phi

### Marketing voi chi phi thap

- Tan dung mang luoi ca nhan
- Tao noi dung viral
- Hop tac voi influencers nho
- Tham gia cong dong

## Buoc 10: Ban hang va cham soc khach hang

### Quy trinh ban hang

```
Lead → Tiep can → Tu van → Chot don → Cham soc sau ban
```

### Cham soc khach hang

- Phan hoi nhanh chong
- Giai quyet van de triet de
- Hoi thong tin phan hoi
- Xay dung chuong trinh khach hang than thiet

## Buoc 11: Quan ly tai chinh

### Nguyen tac quan trong

1. **Tach biet tai khoan** - Ca nhan va doanh nghiep
2. **Theo doi thu chi** - Hang ngay
3. **Kiem soat dong tien** - Cash flow la vua
4. **Du phong** - It nhat 3-6 thang chi phi

### Cong cu quan ly

- Excel/Google Sheets
- Phan mem ke toan (MISA, Fast)
- App quan ly tai chinh

![Financial Management](https://images.unsplash.com/photo-1554224155-8d04cb21cd6e?w=800&q=80)

## Buoc 12: Xay dung doi ngu

### Tuyen dung dung nguoi

- **Culture fit** - Phu hop van hoa cong ty
- **Skills** - Ky nang can thiet
- **Attitude** - Thai do tich cuc
- **Growth mindset** - San sang hoc hoi

### Quan ly doi ngu

- Giao tiep ro rang va thuong xuyen
- Trao quyen va tin tuong
- Ghi nhan va khen thuong
- Tao co hoi phat trien

## Buoc 13: Do luong va phan tich

### KPIs can theo doi

| Nhom | Chi so |
|------|--------|
| Doanh thu | GMV, Revenue, AOV |
| Khach hang | CAC, LTV, Churn rate |
| San pham | DAU, MAU, Retention |
| Marketing | CTR, Conversion, ROAS |

### Cong cu phan tich

- Google Analytics
- Mixpanel
- Hotjar

## Buoc 14: Mo rong quy mo

### Khi nao nen scale?

- San pham da duoc thi truong chap nhan
- Co quy trinh van hanh on dinh
- Dong tien duong
- Co nguon luc (tai chinh, nhan su)

### Cach scale

1. **Mo rong dia ly** - Thi truong moi
2. **Mo rong san pham** - San pham moi
3. **Mo rong kenh** - Kenh ban moi
4. **M&A** - Mua lai hoac sap nhap

## Buoc 15: Kien tri va thich nghi

### Mindset cua nguoi khoi nghiep

- **Kien nhan** - Thanh cong can thoi gian
- **Linh hoat** - San sang thay doi khi can
- **Hoc hoi** - Tu that bai va thanh cong
- **Lac quan** - Nhung thuc te

![Entrepreneurship](https://images.unsplash.com/photo-1507679799987-c73779587ccf?w=800&q=80)

### Xu ly that bai

> "Thanh cong khong phai la khong bao gio that bai, ma la dung day moi khi nga."

1. Phan tich nguyen nhan
2. Rut kinh nghiem
3. Dieu chinh ke hoach
4. Tiep tuc hanh dong

## Ket luan

Khoi nghiep la hanh trinh day thu thach nhung cung day y nghia. 15 buoc tren chi la khung suon - ban can linh hoat ap dung tuy theo hoan canh cu the.

**Dieu quan trong nhat**: HAY BAT DAU NGAY HOM NAY!

Mot y tuong tot ma khong hanh dong thi khong co gia tri. Hay bat dau tu buoc nho nhat va tien len tung buoc mot.

Chuc ban thanh cong tren con duong khoi nghiep!',
  '',
  'https://images.unsplash.com/photo-1559136555-9303baea8ebd?w=1200&q=80',
  'Khoi nghiep startup tu con so 0',
  'startup,kinh doanh,khoi nghiep,entrepreneur,business',
  35, true, true, 'PUBLISHED', 19870,
  '15 buoc khoi nghiep thanh cong tu con so 0 | Tree Blog',
  'Huong dan chi tiet 15 buoc khoi nghiep thanh cong danh cho nguoi moi bat dau. Tu tim y tuong, lap ke hoach den xay dung va mo rong doanh nghiep.',
  'khoi nghiep, startup, kinh doanh, entrepreneur, business plan',
  'c0000000-0000-0000-0000-000000000002',
  (SELECT id FROM users WHERE email = 'admin@tree.com' LIMIT 1),
  false,
  NOW() - INTERVAL '7 days', NOW() - INTERVAL '7 days', NOW() - INTERVAL '7 days'
WHERE NOT EXISTS (SELECT 1 FROM articles WHERE slug = '15-buoc-khoi-nghiep-thanh-cong-tu-con-so-0-danh-cho-nguoi-moi-bat-dau');

-- Article 4: Healthy Lifestyle
INSERT INTO articles (id, title, slug, summary, content, content_html, featured_image, featured_image_alt, tags, reading_time, is_featured, allow_comments, status, view_count, meta_title, meta_description, meta_keywords, category_id, author_id, deleted, created_at, updated_at, published_at)
SELECT
  'b0000000-0000-0000-0000-000000000004',
  '20 thoi quen song khoe giup ban song lau va hanh phuc hon',
  '20-thoi-quen-song-khoe-giup-ban-song-lau-va-hanh-phuc-hon',
  'Tong hop 20 thoi quen song khoe duoc khoa hoc chung minh, giup ban cai thien suc khoe the chat, tinh than va tang tuoi tho.',
  '## Song khoe la gi?

Song khoe khong chi don thuan la khong benh tat. Theo To chuc Y te The gioi (WHO), suc khoe la trang thai hoan toan ve the chat, tinh than va xa hoi.

![Healthy Living](https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=800&q=80)

Bay gio, chung ta se di vao 20 thoi quen cu the de song khoe hon moi ngay.

## Thoi quen an uong

### 1. An sang day du

An sang cung cap nang luong cho ca ngay:

- **Protein**: Trung, sua, dau hu
- **Carbs phuc**: Yen mach, banh mi nguyen cam
- **Trai cay**: Cung cap vitamin va chat xo
- **Nuoc**: 1 ly nuoc am khi thuc day

### 2. An nhieu rau xanh va trai cay

Muc tieu: 5-7 phan rau cu trai cay moi ngay

![Fruits and Vegetables](https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=800&q=80)

**Loi ich:**
- Cung cap vitamin, khoang chat
- Giau chat chong oxy hoa
- Giam nguy co ung thu, tim mach

### 3. Uong du nuoc

Cong thuc tinh luong nuoc can uong:

```
Can nang (kg) x 30ml = Luong nuoc/ngay

Vi du: 60kg x 30ml = 1800ml = 1.8 lit/ngay
```

**Meo:**
- Uong nuoc ngay khi thuc day
- Mang theo binh nuoc
- Dat nhac nho uong nuoc

### 4. Han che duong va thuc an che bien

**Can tranh:**
- Nuoc ngot, tra sua
- Banh keo, snack
- Thuc an dong hop
- Thuc an chien ran

**Thay the bang:**
- Nuoc loc, tra xanh
- Trai cay, hat
- Thuc an tu nhien

### 5. An cham va nhai ky

- Nhai 20-30 lan moi mieng
- Dat dua xuong giua cac lan an
- Tap trung vao bua an, khong xem dien thoai

## Thoi quen van dong

### 6. Tap the duc 30 phut moi ngay

![Exercise](https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=800&q=80)

**Cac hinh thuc tap:**
- Di bo nhanh
- Chay bo
- Boi loi
- Yoga
- Tap gym

### 7. Dung len va di lai moi 30-60 phut

Ngoi lau la "sat thu tham lang":

- Dat bao thuc nhac nho
- Di bo khi nghe dien thoai
- Su dung cau thang thay vi thang may

### 8. Gian co truoc khi ngu

5-10 phut gian co giup:
- Thu gian co bap
- Cai thien giac ngu
- Giam cang thang

## Thoi quen ngu nghi

### 9. Ngu 7-8 tieng moi dem

| Tuoi | Thoi gian ngu |
|------|---------------|
| 18-64 | 7-9 tieng |
| 65+ | 7-8 tieng |

### 10. Di ngu va day dung gio

![Sleep](https://images.unsplash.com/photo-1541781774459-bb2af2f05b55?w=800&q=80)

- Gio ngu ly tuong: 22h-23h
- Gio day ly tuong: 5h30-6h30
- Giu nhat quan ca tuan

### 11. Tao moi truong ngu tot

- Phong toi, yen tinh
- Nhiet do mat (20-22°C)
- Khong dung dien thoai 1 gio truoc khi ngu
- Giuong chi de ngu

## Thoi quen tinh than

### 12. Thien 10 phut moi ngay

**Cac buoc thien co ban:**

1. Ngoi thoai mai, lung thang
2. Nham mat, hit tho sau
3. Tap trung vao hoi tho
4. Khi suy nghi xuat hien, nhe nhang quay lai hoi tho
5. Bat dau voi 5 phut, tang dan len 10-20 phut

### 13. Viet nhat ky biet on

Moi toi, viet ra 3 dieu ban biet on trong ngay:

- Cai thien tam trang
- Giam tram cam
- Tang cam giac hanh phuc

### 14. Gianh thoi gian cho so thich

![Hobbies](https://images.unsplash.com/photo-1513364776144-60967b0f800f?w=800&q=80)

- Doc sach
- Nghe nhac
- Ve tranh
- Lam vuon
- Nau an

### 15. Ket noi voi nguoi than

- Goi dien cho gia dinh thuong xuyen
- Gap go ban be
- Tham gia cong dong
- Giup do nguoi khac

## Thoi quen hang ngay

### 16. Danh rang 2 lan/ngay

- Sang: Sau khi an sang
- Toi: Truoc khi ngu
- Su dung chi nha rang

### 17. Rua tay thuong xuyen

**Khi nao can rua tay:**
- Truoc khi an
- Sau khi di ve sinh
- Sau khi cham vao be mat cong cong
- Sau khi ho hoac hat hoi

### 18. Tiep xuc voi anh nang mat troi

- 15-20 phut moi ngay
- Tot nhat truoc 9h sang hoac sau 16h chieu
- Giup tong hop Vitamin D
- Cai thien tam trang

### 19. Han che ruou bia va thuoc la

**Tac hai:**
- Ung thu
- Benh tim mach
- Ton thuong gan
- Giam tuoi tho

### 20. Kham suc khoe dinh ky

![Health Checkup](https://images.unsplash.com/photo-1579684385127-1ef15d508118?w=800&q=80)

| Tuoi | Tan suat |
|------|----------|
| 18-39 | 2-3 nam/lan |
| 40-49 | 1-2 nam/lan |
| 50+ | Hang nam |

## Bang tom tat thoi quen theo thoi gian trong ngay

### Sang (5h-9h)
- [ ] Uong 1 ly nuoc am
- [ ] Tap the duc 30 phut
- [ ] An sang day du
- [ ] Thien 10 phut

### Trua (12h-14h)
- [ ] An trua can bang
- [ ] Di bo nhe 10-15 phut
- [ ] Nghi ngoi mat (neu co the)

### Chieu (17h-19h)
- [ ] Tap the duc hoac van dong
- [ ] An toi nhe
- [ ] Gianh thoi gian cho gia dinh

### Toi (21h-23h)
- [ ] Gian co 5-10 phut
- [ ] Viet nhat ky biet on
- [ ] Di ngu truoc 23h

## Ket luan

Suc khoe la tai san quy gia nhat ma chung ta co. 20 thoi quen tren khong kho de thuc hien - dieu quan trong la bat dau tu NGAY HOM NAY.

**Loi khuyen:**
1. Khong can lam tat ca cung mot luc
2. Bat dau voi 2-3 thoi quen don gian
3. Tao thanh thoi quen truoc khi them moi
4. Kiem tra va dieu chinh thuong xuyen

> "Chung ta la nhung gi chung ta lap di lap lai. Vi vay, su xuat sac khong phai la hanh dong ma la thoi quen." - Aristotle

Chuc ban luon khoe manh va hanh phuc!',
  '',
  'https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=1200&q=80',
  'Thoi quen song khoe moi ngay',
  'suc khoe,doi song,thoi quen,healthy lifestyle,wellness',
  22, false, true, 'PUBLISHED', 12340,
  '20 thoi quen song khoe giup song lau va hanh phuc | Tree Blog',
  'Tong hop 20 thoi quen song khoe duoc khoa hoc chung minh giup cai thien suc khoe the chat, tinh than va tang tuoi tho.',
  'suc khoe, healthy lifestyle, thoi quen tot, song khoe, wellness',
  'c0000000-0000-0000-0000-000000000003',
  (SELECT id FROM users WHERE email = 'admin@tree.com' LIMIT 1),
  false,
  NOW() - INTERVAL '10 days', NOW() - INTERVAL '10 days', NOW() - INTERVAL '10 days'
WHERE NOT EXISTS (SELECT 1 FROM articles WHERE slug = '20-thoi-quen-song-khoe-giup-ban-song-lau-va-hanh-phuc-hon');

-- Product Categories for Furniture
INSERT INTO categories (id, name, slug, description, active, sort_order, deleted, created_at, updated_at) VALUES
('c1000000-0000-0000-0000-000000000001', 'Ban ghe', 'ban-ghe', 'Ban ghe go tu nhien cao cap', true, 10, false, NOW(), NOW()),
('c1000000-0000-0000-0000-000000000002', 'Giuong ngu', 'giuong-ngu', 'Giuong ngu go tu nhien', true, 11, false, NOW(), NOW()),
('c1000000-0000-0000-0000-000000000003', 'Tu ke', 'tu-ke', 'Tu quan ao, ke sach go tu nhien', true, 12, false, NOW(), NOW()),
('c1000000-0000-0000-0000-000000000004', 'Sofa', 'sofa', 'Sofa go va nem cao cap', true, 13, false, NOW(), NOW())
ON CONFLICT (slug) DO NOTHING;

-- Wood Furniture Products
INSERT INTO products (id, name, slug, summary, description, featured_image, images, price, original_price, sku, dimensions, material, color, weight, stock_quantity, is_featured, is_active, meta_title, meta_description, meta_keywords, view_count, category_id, created_at, updated_at) VALUES
('p0000000-0000-0000-0000-000000000001', 'Ban an go oc cho 6 ghe', 'ban-an-go-oc-cho-6-ghe', 'Ban an go oc cho nguyen khoi, thiet ke hien dai, phu hop gia dinh 4-6 nguoi', 'Ban an duoc lam tu go oc cho (walnut) nguyen khoi, nhap khau tu Bac My. Go oc cho noi tieng voi van go dep tu nhien, do ben cao va mau sac am ap.

Dac diem noi bat:
- Go oc cho nguyen khoi 100%
- Van go tu nhien, khong hai mieng giong nhau
- Xu ly chong moi mot, chong cong venh
- Son PU cao cap, an toan suc khoe
- Thiet ke toi gian, hien dai
- Chan ban chac chan, chiu luc tot

Huong dan bao quan:
- Lau bang khan am mem
- Tranh de vat nong truc tiep
- Danh bong dinh ky 6 thang/lan', 'https://images.unsplash.com/photo-1617806118233-18e1de247200?w=800&q=80', 'https://images.unsplash.com/photo-1615066390971-03e4e1c36ddf?w=800&q=80,https://images.unsplash.com/photo-1595428774223-ef52624120d2?w=800&q=80', 25900000, 32000000, 'BA-OC-001', '180 x 90 x 75 cm', 'Go oc cho nguyen khoi', 'Nau oc cho tu nhien', 85, 5, true, true, 'Ban an go oc cho 6 ghe | Tree Furniture', 'Ban an go oc cho nguyen khoi, thiet ke hien dai cho khong gian phong an sang trong', 'ban an, go oc cho, ban an go, noi that cao cap', 1250, 'c1000000-0000-0000-0000-000000000001', NOW(), NOW()),

('p0000000-0000-0000-0000-000000000002', 'Ghe an go soi khung inox', 'ghe-an-go-soi-khung-inox', 'Ghe an go soi tu nhien, khung inox chac chan, dem ngoi em ai', 'Ghe an ket hop giua go soi tu nhien va khung inox cao cap, tao nen su hoa quyen giua truyen thong va hien dai.

Dac diem noi bat:
- Mat ngoi go soi nguyen tam
- Khung inox 304 khong gi
- Dem ngoi PU cao cap
- Trong luong nhe, de di chuyen
- Phu hop nhieu khong gian

Kich thuoc: 45 x 50 x 85 cm', 'https://images.unsplash.com/photo-1503602642458-232111445657?w=800&q=80', 'https://images.unsplash.com/photo-1506439773649-6e0eb8cfb237?w=800&q=80', 3500000, 4200000, 'GA-SOI-001', '45 x 50 x 85 cm', 'Go soi + Inox 304', 'Go tu nhien', 8, 20, true, true, 'Ghe an go soi khung inox | Tree Furniture', 'Ghe an go soi tu nhien, khung inox cao cap, thiet ke hien dai', 'ghe an, go soi, ghe go, noi that', 980, 'c1000000-0000-0000-0000-000000000001', NOW(), NOW()),

('p0000000-0000-0000-0000-000000000003', 'Giuong ngu go soi 1m8', 'giuong-ngu-go-soi-1m8', 'Giuong ngu go soi nguyen khoi, kich thuoc 1m8, thiet ke toi gian', 'Giuong ngu duoc lam tu go soi tu nhien, mang den giac ngu ngon va khong gian phong ngu am cung.

Dac diem noi bat:
- Go soi Bac Au nguyen khoi
- Ket cau chac chan, chiu luc 500kg
- Thiet ke dau giuong cao, tien loi
- Nan giuong go, thong thoang
- Bao hanh 5 nam

Kich thuoc: 200 x 180 x 100 cm (DxRxC)', 'https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?w=800&q=80', 'https://images.unsplash.com/photo-1540518614846-7eded433c457?w=800&q=80,https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=800&q=80', 18500000, 22000000, 'GN-SOI-180', '200 x 180 x 100 cm', 'Go soi Bac Au', 'Nau go tu nhien', 120, 8, true, true, 'Giuong ngu go soi 1m8 | Tree Furniture', 'Giuong ngu go soi nguyen khoi 1m8, thiet ke hien dai, ben dep', 'giuong ngu, go soi, giuong go, noi that phong ngu', 2100, 'c1000000-0000-0000-0000-000000000002', NOW(), NOW()),

('p0000000-0000-0000-0000-000000000004', 'Tu quan ao 4 canh go cao su', 'tu-quan-ao-4-canh-go-cao-su', 'Tu quan ao 4 canh go cao su, dung luong lon, thiet ke thong minh', 'Tu quan ao go cao su tu nhien, thiet ke 4 canh voi nhieu ngan, phu hop cho gia dinh.

Dac diem noi bat:
- Go cao su tu nhien xu ly ki
- 4 canh voi guong soi toan than
- Nhieu ngan treo va ke
- Ray truot giam chan cao cap
- Khoa an toan

Kich thuoc: 200 x 60 x 220 cm', 'https://images.unsplash.com/photo-1558997519-83ea9252edf8?w=800&q=80', 'https://images.unsplash.com/photo-1595526114035-0d45ed16cfbf?w=800&q=80', 12900000, 15500000, 'TQA-CS-004', '200 x 60 x 220 cm', 'Go cao su tu nhien', 'Trang sua', 150, 6, false, true, 'Tu quan ao 4 canh go cao su | Tree Furniture', 'Tu quan ao go cao su 4 canh, dung luong lon, thiet ke hien dai', 'tu quan ao, go cao su, tu go, noi that phong ngu', 850, 'c1000000-0000-0000-0000-000000000003', NOW(), NOW()),

('p0000000-0000-0000-0000-000000000005', 'Ke sach go thong 5 tang', 'ke-sach-go-thong-5-tang', 'Ke sach go thong 5 tang, thiet ke toi gian, de lap dat', 'Ke sach go thong tu nhien, thiet ke Bac Au, phu hop nhieu khong gian.

Dac diem noi bat:
- Go thong Nga nguyen khoi
- Thiet ke mo 5 tang
- De lap dat, co the thao roi
- Son PU an toan
- Chiu luc tot (moi tang 30kg)

Kich thuoc: 80 x 30 x 180 cm', 'https://images.unsplash.com/photo-1594620302200-9a762244a156?w=800&q=80', 'https://images.unsplash.com/photo-1507842217343-583bb7270b66?w=800&q=80', 4500000, 5500000, 'KS-TH-005', '80 x 30 x 180 cm', 'Go thong Nga', 'Go tu nhien', 25, 15, false, true, 'Ke sach go thong 5 tang | Tree Furniture', 'Ke sach go thong 5 tang, thiet ke Bac Au, de lap dat', 'ke sach, go thong, ke go, noi that', 620, 'c1000000-0000-0000-0000-000000000003', NOW(), NOW()),

('p0000000-0000-0000-0000-000000000006', 'Sofa go oc cho 3 cho ngoi', 'sofa-go-oc-cho-3-cho-ngoi', 'Sofa go oc cho 3 cho ngoi, nem boc da that, sang trong', 'Sofa go oc cho nguyen khoi ket hop nem boc da that, mang den su sang trong va thoai mai.

Dac diem noi bat:
- Khung go oc cho nguyen khoi
- Nem mut D40 cao cap
- Boc da bo that Y
- Thiet ke ergonomic
- Chan go chac chan

Kich thuoc: 210 x 85 x 80 cm', 'https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=800&q=80', 'https://images.unsplash.com/photo-1493663284031-b7e3aefcae8e?w=800&q=80,https://images.unsplash.com/photo-1550254478-ead40cc54513?w=800&q=80', 35000000, 42000000, 'SF-OC-003', '210 x 85 x 80 cm', 'Go oc cho + Da bo that', 'Nau + Den', 95, 3, true, true, 'Sofa go oc cho 3 cho ngoi | Tree Furniture', 'Sofa go oc cho nguyen khoi, nem da that, thiet ke sang trong', 'sofa, go oc cho, sofa go, noi that phong khach', 1850, 'c1000000-0000-0000-0000-000000000004', NOW(), NOW()),

('p0000000-0000-0000-0000-000000000007', 'Ban tra go huong da mat kinh', 'ban-tra-go-huong-da-mat-kinh', 'Ban tra go huong da ket hop mat kinh cuong luc, sang trong', 'Ban tra go huong da quy, ket hop mat kinh cuong luc, tao diem nhan cho phong khach.

Dac diem noi bat:
- Go huong da Viet Nam
- Mat kinh cuong luc 10mm
- Van go doc dao, quy hiem
- Thiet ke co dien ket hop hien dai
- Bao hanh 10 nam

Kich thuoc: 120 x 60 x 45 cm', 'https://images.unsplash.com/photo-1499933374294-4584851497cc?w=800&q=80', 'https://images.unsplash.com/photo-1532372320572-cda25653a26d?w=800&q=80', 28000000, 35000000, 'BT-HD-001', '120 x 60 x 45 cm', 'Go huong da + Kinh cuong luc', 'Do sam', 65, 2, true, true, 'Ban tra go huong da mat kinh | Tree Furniture', 'Ban tra go huong da quy, mat kinh cuong luc, thiet ke sang trong', 'ban tra, go huong, ban go, noi that cao cap', 980, 'c1000000-0000-0000-0000-000000000004', NOW(), NOW()),

('p0000000-0000-0000-0000-000000000008', 'Ban lam viec go soi chan sat', 'ban-lam-viec-go-soi-chan-sat', 'Ban lam viec go soi, chan sat son tinh dien, hien dai', 'Ban lam viec thiet ke hien dai, mat ban go soi nguyen tam, chan sat chac chan.

Dac diem noi bat:
- Mat ban go soi nguyen tam 3cm
- Chan sat son tinh dien
- Ngan keo 2 ben tien dung
- Ho day cap tich hop
- Phu hop lam viec tai nha

Kich thuoc: 140 x 70 x 75 cm', 'https://images.unsplash.com/photo-1518455027359-f3f8164ba6bd?w=800&q=80', 'https://images.unsplash.com/photo-1593062096033-9a26b09da705?w=800&q=80', 8500000, 10000000, 'BLV-SOI-001', '140 x 70 x 75 cm', 'Go soi + Sat son tinh dien', 'Go tu nhien + Den', 45, 12, false, true, 'Ban lam viec go soi chan sat | Tree Furniture', 'Ban lam viec go soi nguyen tam, thiet ke hien dai, phu hop home office', 'ban lam viec, go soi, ban go, noi that van phong', 750, 'c1000000-0000-0000-0000-000000000001', NOW(), NOW())
ON CONFLICT (slug) DO NOTHING;

-- Site Settings
INSERT INTO site_settings (id, site_name, site_tagline, site_description, logo_url, logo_dark_url, favicon_url, contact_email, contact_phone, contact_address, facebook_url, instagram_url, youtube_url, tiktok_url, zalo_url, footer_text, copyright_text, hero_title, hero_subtitle, category_section_title, category_section_subtitle, service_section_title, services_json, created_at, updated_at) VALUES
('e0000000-0000-0000-0000-000000000001', 'Duc Viet', 'Tinh Hoa Tram Huong', 'Chuyen cung cap vong tay tram huong, tuong phat, nhang tram, tinh dau tram huong thien nhien 100%', '/uploads/logo.png', '/uploads/logo-dark.png', '/uploads/favicon.ico', 'contact@ducviet.com', '0909 123 456', '123 Nguyen Hue, Quan 1, TP.HCM', 'https://facebook.com/ducviet', 'https://instagram.com/ducviet', 'https://youtube.com/@ducviet', 'https://tiktok.com/@ducviet', 'https://zalo.me/ducviet', 'Duc Viet - Tinh hoa tram huong Viet Nam', '2024 Duc Viet. All rights reserved.', 'DUC VIET', 'Tinh Hoa Thien Nhien Viet Nam', 'Tinh Hoa Tram Huong', 'Curated By Duc Viet', 'DUC VIET SERVICES', '[{"title":"GIAO HANG TAN NOI","description":"Mien phi giao hang toan quoc cho don hang tu 2 trieu dong.","imageUrl":"/uploads/products/vong-tay-tram-huong-2.jpg","linkText":"Tim Hieu Them","linkUrl":"/about"},{"title":"TU VAN CHUYEN GIA","description":"Doi ngu chuyen gia tram huong tu van mien phi.","imageUrl":"/uploads/products/tuong-phat-tram-huong-1.jpg","linkText":"Lien He Ngay","linkUrl":"/contact"},{"title":"BAO HANH TRON DOI","description":"Cam ket bao hanh tron doi cho tat ca san pham tram huong.","imageUrl":"/uploads/products/nhang-tram-huong.jpg","linkText":"Chinh Sach Bao Hanh","linkUrl":"/warranty"}]', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;
