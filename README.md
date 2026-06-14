# WebClip 快捷方式生成器

## 自动构建 APK（无需安装 Android Studio）

### 步骤

1. **Fork 本仓库到您的 GitHub 账号**
   - 点击页面右上角的 Fork 按钮

2. **启用 GitHub Actions**
   - 进入您 fork 的仓库
   - 点击 Settings → Actions → General
   - 选择 "Allow all actions and reusable workflows"
   - 点击 Save

3. **触发构建**
   - 点击 Actions 标签
   - 选择 "Build APK" 工作流
   - 点击 "Run workflow" → "Run workflow"

4. **下载 APK**
   - 等待构建完成（约 3-5 分钟）
   - 点击最新的工作流运行记录
   - 在 Artifacts 部分下载 "WebClipMaker-APK"
   - 解压下载的文件即可获得 APK

### 手动构建（需要 Android Studio）

1. 用 Android Studio 打开项目
2. 连接手机或启动模拟器
3. 点击 Run → Run 'app'
4. 或 Build → Generate Signed Bundle/APK

## 功能说明

- 输入任意网址生成桌面快捷方式
- 自定义快捷方式名称和图标
- 使用完成后可一键卸载

## 系统要求

- Android 7.0 (API 24) 及以上
