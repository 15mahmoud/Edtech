# استخدم صورة رسمية لـ Node.js
FROM node:20

# تحديد مجلد العمل داخل الحاوية
WORKDIR /app

# نسخ ملفات الباكيج فقط لتحسين الكاش
COPY package.json package-lock.json ./

# تثبيت الحزم
RUN npm install

# نسخ باقي ملفات المشروع
COPY . .

# تعيين المنفذ
EXPOSE 5000

# تشغيل التطبيق
CMD ["npm", "run", "dev"]
