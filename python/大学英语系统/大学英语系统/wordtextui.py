import tkinter as tk
import sqlite3
import random
from tkinter import messagebox
import pyttsx3
import time
# 单词测试功能
class wordtext_ui():
    def __init__(self,root):
        self.root = root
        self.count = 1
        # 用来显示正确的个数
        self.success = 0
        self.text_list = []
        self.display()
        pass

    # todo  清空画布功能
    def clear_frame(self,frame):
        for label in frame.winfo_children():
            label.destroy()
            pass
        pass
    # todo 发音功能
    def method_nouce(self):
        gen = pyttsx3.init()
        gen.say(self.word[0])
        gen.runAndWait()
        pass

    # todo 选项设置
    def method_op(self,word):
        if self.count == int(self.var_cs_word.get()):
            # 用来显示测试结果
            messagebox.showinfo("提示","测试结束,正确：{},错误：{}".format(self.success,self.count-self.success))
            # 恢复默认
            self.count = 1
            self.success = 0
            self.clear_frame(self.top_frame)
            self.score = 0
            self.score_lab_write.configure(text = str(self.score))
            return
            pass
        print(word)
        print(self.word)
        if self.word == word:
            self.success+=1
            # 用来更新 分数
            self.score = (self.success / int(self.var_cs_word.get())) * 100
            self.score_lab_write.configure(text=str(self.score))
            pass
        # 更新操作
        self.word = random.choice(self.book_list)
        # print(word)
        # 将抽到 单词放到 list中
        self.text_list.append(self.word)
        # print(self.text_list)
        # 然后抽取  2个单词作为干扰项
        list1 = list()
        i = 1
        while i<=2:
            word01 = random.choice(self.book_list)
            if word01 not in self.text_list and word01 not in list1:
                list1.append(word01)
                i = i+1
            else:
                continue
            pass
        list1.append(self.word)
        print(list1)
        word001 = random.choice(list1)
        # print(word001)
        list1.remove(word001)
        word002 = random.choice(list1)
        list1.remove(word002)
        print(list1)
        word003 = random.choice(list1)
        list1.remove(word003)
        # 更改标签的信息
        self.count+=1
        self.pro_id.configure(text = '第    {}    题'.format(self.count))
        self.pro_lab.configure(text =self.word[0])
        self.but_option01.configure(text = word001[1],command = lambda :self.method_op(word001))
        self.but_option02.configure(text = word002[1],command = lambda :self.method_op(word002))
        self.but_option03.configure(text = word003[1],command = lambda :self.method_op(word003))
        pass
    # todo 听力选项
    def method_op_listen(self,word):
        if not self.book_name:
            messagebox.showinfo("提示","请选择你要使用的单词英语书")
            return
        # 大致思路 同单词测试选项
        if self.count == int(self.var_cs_word.get()):
            # 用来显示测试结果
            messagebox.showinfo("提示","测试结束,正确：{},错误：{}".format(self.success,self.count-self.success))
            # 恢复默认
            self.count = 1
            self.success = 0
            self.clear_frame(self.top_frame)
            self.score = 0
            self.score_lab_write.configure(text=str(self.score))
            return
            pass
        print(word)
        print(self.word)
        if self.word == word:
            self.success+=1
            # 用来更新 分数
            self.score = (self.success/int(self.var_cs_word.get()))*100
            self.score_lab_write.configure(text = str(self.score))
            pass
        # 更新操作
        self.word = random.choice(self.book_list)
        # print(word)
        # 将抽到 单词放到 list中
        self.text_list.append(self.word)
        # print(self.text_list)
        # 然后抽取  2个单词作为干扰项
        list1 = list()
        i = 1
        while i<=2:
            word01 = random.choice(self.book_list)
            if word01 not in self.text_list and word01 not in list1:
                list1.append(word01)
                i = i+1
            else:
                continue
            pass
        list1.append(self.word)
        print(list1)
        word001 = random.choice(list1)
        # print(word001)
        list1.remove(word001)
        word002 = random.choice(list1)
        list1.remove(word002)
        print(list1)
        word003 = random.choice(list1)
        list1.remove(word003)
        # 更改标签的信息
        self.count+=1
        self.pro_id.configure(text = '第    {}    题'.format(self.count))
        if self.count% 2 == 0:
            self.but_option01.configure(text = word001[1],command = lambda :self.method_op_listen(word001))
            self.but_option02.configure(text = word002[1],command = lambda :self.method_op_listen(word002))
            self.but_option03.configure(text = word003[1],command = lambda :self.method_op_listen(word003))
        else:
            self.but_option01.configure(text=word001[0], command=lambda: self.method_op_listen(word001))
            self.but_option02.configure(text=word002[0], command=lambda: self.method_op_listen(word002))
            self.but_option03.configure(text=word003[0], command=lambda: self.method_op_listen(word003))

        pass

    # todo 设置功能确定方法
    def method_set_ok(self):
        self.top_root.destroy()
        pass

    # TODO 设置功能
    def method_set(self):
        if not self.book_name:
            messagebox.showinfo("提示","请选择你要使用的单词英语书")
            return
        self.top_root = tk.Toplevel()
        self.top_root.title("设置")
        self.top_root.geometry("300x200+500+200")
        self.top_root.resizable(False,False)
        # 一个标签
        self.lab_word_name = tk.Label(self.top_root,text = '测试单词数量',font = ('微软雅黑',18))
        self.lab_word_name.place(relx=0.15, rely=0.05, relheight=0.2, relwidth=0.8)

        # 下面一个用来记录单词的数量,默认是 10 个单词
        # 创建 scale 滚动条
        self.spin_box = tk.Spinbox(self.top_root,from_= 10 ,to = 100,increment = 10, textvariable = self.var_cs_word)
        self.spin_box.place(relx=0.15, rely=0.35, relheight=0.2, relwidth=0.8)
        # 创建一个 确定 按钮
        self.but_ok = tk.Button(self.top_root,text ='确 定',font=("宋体",20),command = self.method_set_ok)
        self.but_ok.pack(side = "bottom")




        pass
    # todo 获取当前的单词书
    def get_book(self):
        with sqlite3.connect("User.db") as coon :
            cur = coon.cursor()
            sql = "select bname from book where version = 1"
            cur.execute(sql)
            self.book_name = cur.fetchall()
            pass
        if self.book_name:
            self.book_name = self.book_name[0][0]
        else:
            self.book_name = []
            pass
        pass

    # todo 听力测试
    def listen_text(self):
        if not self.book_name:
            messagebox.showinfo("提示","请选择你要使用的单词英语书")
            return
        # 基本布局大致和 单词测试一致
        if not self.var_cs_word.get():
            messagebox.showinfo("提示", "请先选择，你要测试的单词数")
            return
        # 首先要先清空画布
        self.clear_frame(self.top_frame)
        #  首先 第一个标签是 为了 存放 题号 的
        self.pro_id = tk.Label(self.top_frame, text='第    {}    题'.format(self.count), font=('微软雅黑', 24))
        self.pro_id.place(relx=0.0, rely=0.0, relheight=0.2, relwidth=1)

        # 从数据库中获取相应的单词
        with sqlite3.connect("User.db") as coon:
            cur = coon.cursor()
            sql = "select *from {}".format(self.book_name)
            cur.execute(sql)
            self.book_list = cur.fetchall()
            pass
        # print(self.book_list)
        # 随机的抽取一个单词
        self.word = random.choice(self.book_list)
        # print(word)
        # 将抽到 单词放到 list中
        self.text_list.append(self.word)
        # print(self.text_list)
        # 然后抽取  2个单词作为干扰项
        list1 = list()
        i = 1
        while i <= 2:
            word01 = random.choice(self.book_list)
            if word01 not in self.text_list and word01 not in list1:
                list1.append(word01)
                i += 1
            else:
                continue
            pass

        self.img_voice = tk.PhotoImage(file ="image/4_02.gif")

        # 首先 先显示题目
        self.but_nounce = tk.Button(self.top_frame,image = self.img_voice,command = self.method_nouce,bd = 0)
        self.but_nounce.place(relx=0.0, rely=0.2, relheight=0.2, relwidth=1)
        # 将题目和干扰项放入
        list1.append(self.word)

        word001 = random.choice(list1)
        # print(word001)
        list1.remove(word001)
        word002 = random.choice(list1)
        list1.remove(word002)
        # print(list1)
        word003 = random.choice(list1)
        list1.remove(word003)
        # print(word001,word002,word003)
        # 然后将每一个选项进行写入
        self.but_option01 = tk.Button(self.top_frame, text=word001[0], font=('微软雅黑', 15),
                                      command=lambda: self.method_op_listen(word001),fg= "#333333")
        self.but_option01.place(relx=0.0, rely=0.4, relheight=0.2, relwidth=1)

        self.but_option02 = tk.Button(self.top_frame, text=word002[0], font=('微软雅黑', 15),
                                      command=lambda: self.method_op_listen(word002),fg= "#333333")
        self.but_option02.place(relx=0.0, rely=0.6, relheight=0.2, relwidth=1)

        self.but_option03 = tk.Button(self.top_frame, text=word003[0], font=('微软雅黑', 15),
                                      command=lambda: self.method_op_listen(word003),fg= "#333333")
        self.but_option03.place(relx=0.0, rely=0.8, relheight=0.2, relwidth=1)

        pass
    # todo 单词测试
    def word_text(self):
        if not self.book_name:
            messagebox.showinfo("提示", "请选择你要使用的单词英语书")
            return
        if not self.var_cs_word.get():
            messagebox.showinfo("提示","请先选择，你要测试的单词数")
            return
        # 首先要先清空画布
        self.clear_frame(self.top_frame)
        #  首先 第一个标签是 为了 存放 题号 的
        self.pro_id = tk.Label(self.top_frame,text = '第    {}    题'.format(self.count),font = ('微软雅黑',24))
        self.pro_id.place(relx=0.0, rely=0.0, relheight=0.2, relwidth=1)

        # 从数据库中获取相应的单词
        with sqlite3.connect("User.db") as coon:
            cur  = coon.cursor()
            sql = "select *from {}".format(self.book_name)
            cur.execute(sql)
            self.book_list = cur.fetchall()
            pass
        # print(self.book_list)
        # 随机的抽取一个单词
        self.word = random.choice(self.book_list)
        # print(word)
        # 将抽到 单词放到 list中
        self.text_list.append(self.word)
        # print(self.text_list)
        # 然后抽取  2个单词作为干扰项
        list1 = list()
        i = 1
        while i<=2:
            word01 = random.choice(self.book_list)
            if word01 not in self.text_list and word01 not in list1:
                list1.append(word01)
                i+=1
            else:
                continue
            pass

        # 首先 先显示题目
        self.pro_lab = tk.Label(self.top_frame,text = self.word[0] , font =('微软雅黑',20))
        self.pro_lab.place(relx=0.0, rely=0.2, relheight=0.2, relwidth=1)
        # 将题目和干扰项放入
        list1.append(self.word)

        word001  = random.choice(list1)
            # print(word001)
        list1.remove(word001)
        word002 = random.choice(list1)
        list1.remove(word002)
        # print(list1)
        word003 = random.choice(list1)
        list1.remove(word003)
        # print(word001,word002,word003)
        # 然后将每一个选项进行写入
        self.but_option01 = tk.Button(self.top_frame,text= word001[1] ,font =('微软雅黑',15),command = lambda :self.method_op(word001),fg= "#333333")
        self.but_option01.place(relx=0.0, rely=0.4, relheight=0.2, relwidth=1)

        self.but_option02 = tk.Button(self.top_frame, text=word002[1], font=('微软雅黑', 15),command = lambda :self.method_op(word002),fg= "#333333")
        self.but_option02.place(relx=0.0, rely=0.6, relheight=0.2, relwidth=1)

        self.but_option03 = tk.Button(self.top_frame, text=word003[1], font=('微软雅黑', 15),command = lambda :self.method_op(word003),fg= "#333333")
        self.but_option03.place(relx=0.0, rely=0.8, relheight=0.2, relwidth=1)
        pass

    def display(self):

        self.bg_frame = tk.Frame(self.root,bg ='#d7f3fe')
        self.bg_frame.place(relx=0.0, rely=0.0, relheight=1, relwidth=1)

        # 单词测试标签
        self.text_lab = tk.Label(self.bg_frame,fg = "#333333",bg ='#d7f3fe',text = '单词测试',font = ('宋体',20,'bold'),anchor ='w')
        self.text_lab.place(relx=0.05, rely=0.05, relheight=0.1, relwidth=0.2)
        # # 时间 、 成绩 标签
        # self.time_lab = tk.Label(self.bg_frame,fg = "#383539",bg ='#d7f3fe',text = 'time:',font = ('微软雅黑',16,'bold'),anchor ='w')
        # self.time_lab.place(relx=0.65, rely=0.05, relheight=0.1, relwidth=0.07)
        # # 用来记录时间
        #
        # # self.now = time.strftime("%H:%M")
        #
        # # 一个时间标签
        # self.time_lab_write = tk.Label(self.bg_frame,fg = "#383539",bg ='#d7f3fe',text = self.now,font = ('微软雅黑',16,'bold'),anchor ='w')
        # self.time_lab_write.place(relx=0.72, rely=0.05, relheight=0.1, relwidth=0.07)
        # 成绩标签
        self.score_lab = tk.Label(self.bg_frame, fg="#383539", bg='#d7f3fe', text='score:', font=('微软雅黑', 16, 'bold'), anchor='w')
        self.score_lab.place(relx=0.72, rely=0.05, relheight=0.1, relwidth=0.07)
        # 用来记录分数
        self.score = 0
        self.score_lab_write = tk.Label(self.bg_frame, fg="#383539", bg='#d7f3fe', text=str(self.score), font=('微软雅黑', 16, 'bold'), anchor='w')
        self.score_lab_write.place(relx=0.80, rely=0.05, relheight=0.1, relwidth=0.07)
        self.top_frame = tk.Frame(self.bg_frame,bg = '#f5f5f5')
        self.top_frame.place(relx=0.1, rely=0.2, relheight=0.7, relwidth=0.8)

        self.var_cs_word = tk.StringVar()
        # 默认为 10
        self.var_cs_word.set("")
        #加载  英语 单词 书
        self.get_book()
        # print(self.book_name)

        # 三个按钮 分别是 设置 、发音测试 、 单词测试
        self.but_set = tk.Button(self.bg_frame,text = '设  置',font  = ('宋体',24),command = self.method_set,bg = "#6cc433",fg = "#ffffff")
        self.but_set.place(relx=0.15, rely=0.92, relheight=0.06, relwidth=0.2)

        self.but_text_voice = tk.Button(self.bg_frame, text='听力测试', font=('宋体', 24),command = self.listen_text,bg = "#6cc433",fg = "#ffffff")
        self.but_text_voice.place(relx=0.4, rely=0.92, relheight=0.06, relwidth=0.2)

        self.but_text_word = tk.Button(self.bg_frame, text='单词测试', font=('宋体', 24),command = self.word_text,bg = "#6cc433",fg = "#ffffff")
        self.but_text_word.place(relx=0.65, rely=0.92, relheight=0.06, relwidth=0.2)

        # # 从布局上还是分为 上下两个部分



        pass
    pass

