import tkinter as tk
import tkinter.messagebox
import sqlite3
import serchui
import setui
import learnui
import aboutui
import wordtextui
import wordlistui
import os

# 一个全局表量

# 创建新的主界面图
class main_ui():
    def __init__(self,name):
        # 创建窗口
        self.root = tk.Tk()
        self.name = name
        # 设计窗口
        self.display()
        #显示
        self.root.mainloop()
        pass
    # todo 跟换头像 具体操作
    def method_update_head(self,path):
        global img_head08
        img_head08 = tk.PhotoImage(file = path)
        self.but_head.configure(image =  img_head08)
        # 同时将 头像 的地址记录在数据库中
        with sqlite3.connect("{}.db".format(self.name)) as coon :
            cur = coon.cursor()
            sql  = "update login set image = '{}'".format(path)
            cur.execute(sql)
            pass
        self.top_frame.destroy()
        pass
    # TODO 注销用户操作
    def destory_user(self):
        # 首先 获得 当前 路径
        path = os.getcwd()
        # print(path)
        # 注销用户
        os.remove(path+"\\{}.db".format(self.name))
        # 同时从 主数据库中删除 用户的信息
        with sqlite3.connect("User.db") as coon:
            cur = coon.cursor()
            sql = "delete from login where username = '{}'".format(self.name)
            cur.execute(sql)
            pass
        tk.messagebox.showinfo("提示","该用户已经成功注销")
        # 主窗口 关闭
        self.root.destroy()

        pass

    # todo  换头像的操作
    def update_head(self):
        # 首先 会弹出一个弹窗
        self.top_frame = tk.Toplevel()
        self.top_frame.title("更换头像")
        self.top_frame.geometry("380x200+400+150")
        self.top_frame.configure(bg="#fff")
        path = "Head/1_05.gif"
        # 准备好相应的头像
        self.img_head01  = tk.PhotoImage(file =path)
        self.img_head02 = tk.PhotoImage(file="Head/1_03.gif")
        self.img_head03 = tk.PhotoImage(file="Head/1_07.gif")
        self.img_head04 = tk.PhotoImage(file="Head/1_09.gif")
        self.img_head05 = tk.PhotoImage(file="Head/1_11.gif")
        self.img_head06 = tk.PhotoImage(file="Head/1_18.gif")
        self.img_head07 = tk.PhotoImage(file="Head/1_20.gif")
        self.img_head08 = tk.PhotoImage(file="Head/1_33.gif")

        # img_head08 =  tk.PhotoImage(file="Head/1_33.gif")


        # 与相应的按钮进行绑定
        self.but_head02 = tk.Button(self.top_frame,image = self.img_head02,bg ="#fff",bd = 0,command = lambda:self.method_update_head("Head/1_03.gif"))
        self.but_head02.grid(row = 0, column = 0)
        self.but_head03 = tk.Button(self.top_frame, image=self.img_head03, bg="#fff", bd=0,command = lambda :self.method_update_head("Head/1_07.gif"))
        self.but_head03.grid(row = 0,column = 1)
        self.but_head04 = tk.Button(self.top_frame, image=self.img_head04, bg="#fff", bd=0,command = lambda :self.method_update_head("Head/1_09.gif"))
        self.but_head04.grid(row=0, column=2)
        self.but_head05 = tk.Button(self.top_frame, image=self.img_head05, bg="#fff", bd=0,command = lambda :self.method_update_head("Head/1_11.gif"))
        self.but_head05.grid(row=0, column=3)
        self.but_head06 = tk.Button(self.top_frame, image=self.img_head06, bg="#fff", bd=0,command = lambda :self.method_update_head("Head/1_18.gif"))
        self.but_head06.grid(row=1, column=0)
        self.but_head07 = tk.Button(self.top_frame, image=self.img_head07, bg="#fff", bd=0,command = lambda :self.method_update_head("Head/1_20.gif"))
        self.but_head07.grid(row=1, column=1)
        self.but_head01 = tk.Button(self.top_frame, image=self.img_head01, bg="#fff", bd=0,command = lambda :self.method_update_head("Head/1_05.gif"))
        self.but_head01.grid(row=1, column=2)


        self.but_head08 = tk.Button(self.top_frame, image=self.img_head08, bg="#fff", bd=0,command = self.destory_user)
        self.but_head08.grid(row=1, column=3)

        pass

    def display(self):
        # 设置大小
        self.root.geometry("1200x700+200+50")
        # 设置不可变
        self.root.resizable(False,False)
        # 设置标题
        self.root.title("大学英语学习")
        # 为新的标签创建新的frame布局
        # 用来存放功能的frame
        self.menu_frame = tk.Frame(self.root)
        # 设置摆放位置
        self.menu_frame.place(relx=0.0, rely=0.22, relheight=0.78, relwidth=0.234)
        # 图片布局
        self.img_frame = tk.Frame(self.root,bg ="#177bd0")
        self.img_frame.place(relx=0.0, rely=0.0, relheight=0.22, relwidth=0.234)
        # 将图片布局分为两部分 ， 一部分是 logo 图标处 ，另一部分是 用户信息部分
        self.logo_frame = tk.Frame(self.img_frame,bg = "#fff")
        self.logo_frame.place(relx=0.0, rely=0.0, relheight=1, relwidth=0.4)

        # 获取登录次数
        if self.name == 'admin':
            count = "null"
            pass
        else:
            # 从数据库中进行取出登录的次数 和 自己的头像
            with sqlite3.connect("{}.db".format(self.name)) as coon:
                cur = coon.cursor()
                sql01 = "update login set count = count +1"
                sql ="select count from login where username = '{}'".format(self.name)
                sql02  = "select image from login where username = '{}'".format(self.name)
                cur.execute(sql01)
                cur.execute(sql)
                count = cur.fetchone()[0]
                cur.execute(sql02)
                image = cur.fetchone()[0]
                pass
            pass
        # 首先需要一个图片
        if self.name == "admin":
            self.img_head = tk.PhotoImage(file="Head/tx.gif")
            self.but_head = tk.Button(self.logo_frame, image=self.img_head, bd=0, bg="#c4ccdf", anchor='n')
            self.but_head.place(relx=0.0, rely=0.0, relheight=0.6, relwidth=1)
            pass
        else:
            self.img_head = tk.PhotoImage(file =image)
            self.but_head = tk.Button(self.logo_frame,image = self.img_head,bd = 0,bg = "#fff",anchor = 'n',command = self.update_head)
            self.but_head.place(relx=0.0, rely=0.0, relheight=0.6, relwidth=1)
        # 下方放置一个内容用来填充
        self.lab_up = tk.Label(self.logo_frame,text = "加油",font =('方正舒体',30,"bold"),bd = 0 , bg = "#fff")
        self.lab_up.place(relx=0.0, rely=0.6, relheight=0.4, relwidth=1)



        self.user_info = tk.Frame(self.img_frame,bg ="#177bd0")
        self.user_info.place(relx=0.4, rely=0.0, relheight=1, relwidth=0.6)

        self.label_time = tk.Label(self.user_info,text ='用户名:',font = ('宋体',12),fg = "#e7f2fa",bg ="#177bd0",anchor = 'w')
        self.label_time.place(relx=0.05, rely=0.15, relheight=0.2, relwidth=0.4)

        self.label_otime = tk.Label(self.user_info, text=self.name, font=('宋体', 12),fg = "#e7f2fa",bg ="#177bd0",anchor ='w')
        self.label_otime.place(relx=0.45, rely=0.15, relheight=0.2, relwidth=0.55)

        # 从数据库中获得单词数和登录次数
        with sqlite3.connect("User.db") as coon:
            cur = coon.cursor()
            sql ="select bname from book where version =1"
            cur.execute(sql)
            bname = cur.fetchone()
            pass
        if not bname:
            self.b_name = "无"
            pass
        else:
            self.b_name = bname[0]
            pass

        self.label_words = tk.Label(self.user_info, text='单词书：', font=('宋体', 12),fg = "#e7f2fa",bg ="#177bd0",anchor = 'w')
        self.label_words.place(relx=0.05, rely=0.4, relheight=0.2, relwidth=0.4)

        self.label_owords = tk.Label(self.user_info, text=self.b_name, font=('宋体', 12),fg = "#e7f2fa",bg ="#177bd0",anchor = 'w')
        self.label_owords.place(relx=0.45, rely=0.4, relheight=0.2, relwidth=0.55)


        self.label_score = tk.Label(self.user_info, text='登录次数：', font=('宋体', 12),fg = "#e7f2fa",anchor = 'e',bg ="#177bd0")
        self.label_score.place(relx=0.05, rely=0.65, relheight=0.2, relwidth=0.5)

        self.label_oscore = tk.Label(self.user_info, text=count, font=('宋体', 12),fg = "#e7f2fa",bg ="#177bd0",anchor = 'w')
        self.label_oscore.place(relx=0.65, rely=0.65, relheight=0.2, relwidth=0.3)

        # 具体实现功能的布局
        self.op_frame = tk.Frame(self.root,background="#fff")
        self.op_frame.place(relx=0.234,rely=0.0,relheight=1.00,relwidth=0.766)
        # 在左侧功能栏中添加功能按钮
        # 第一个功能，在线搜词功能
        # 为按钮 导入图像
        self.translate_img_op = tk.PhotoImage(file="image/3_06.gif")
        self.translate_img  = tk.PhotoImage(file = "image/9_06.gif")
        self.btu_search = tk.Button(self.menu_frame,text='在线翻译',font=('宋体',24),anchor='c',command = self.select_search,bg ="#177bd0",fg = "#fff",bd = 0) # anchor 是用来是文字居中显示的
        self.img_search = tk.Button(self.menu_frame,image = self.translate_img,bg='#177bd0',bd = 0,command = self.select_search)
        self.btu_search.place(relx=0.15,rely=0.0,relheight=0.166,relwidth=0.85)
        self.img_search.place(relx=0,rely=0.0,relheight=0.166,relwidth=0.15)
        # 第二个功能单词学习功能
        self.btu_learn  = tk.Button(self.menu_frame,text='单词学习',font=('宋体',24),anchor='c',command = self.select_learn,bg ="#177bd0",fg = "#fff",bd = 0)
        # self.btu_learn.configure(width=19, height=2)
        # 单词学习功能的图片
        self.learn_image = tk.PhotoImage(file ="image/3_10.gif")
        self.learn_image_op = tk.PhotoImage(file = "image/9_08.gif")
        self.img_learn = tk.Button(self.menu_frame,image = self.learn_image,bg ="#177bd0",command = self.select_learn,bd = 0)
        self.img_learn.place(relx=0,rely=0.166,relheight=0.166,relwidth=0.15)
        self.btu_learn.place(relx=0.15,rely=0.166,relheight=0.166,relwidth=0.85)
        # self.btu_learn.grid(row=1)
        # 第三个功能，单词本功能
        self.btu_words = tk.Button(self.menu_frame,text='单 词 本',font=('宋体',24),anchor='c',command = self.select_words,bg ="#177bd0",fg = "#fff",bd = 0)
        # self.btu_words.configure(width=19, height=2)
        # self.btu_words.grid(row=2)
        self.words_img = tk.PhotoImage(file ="image/3_08.gif")
        self.words_img_op = tk.PhotoImage(file = "image/9_04.gif")
        self.img_words = tk.Button(self.menu_frame,image = self.words_img,bg ="#177bd0",command = self.select_words,bd = 0)

        self.btu_words.place(relx=0.15,rely=0.332,relheight=0.166,relwidth=0.85)
        self.img_words.place(relx=0,rely=0.332,relheight=0.166,relwidth=0.15)

        # 第四个功能单词测试
        self.btu_text = tk.Button(self.menu_frame, text='单词测试', font=('宋体', 24), anchor='c',command = self.select_text,bg = "#177bd0",fg = "#fff",bd = 0)
        self.btu_text.configure(width=19, height=2)
        self.text_img = tk.PhotoImage(file= "image/3_12.gif")
        self.text_img_op = tk.PhotoImage(file ="image/9_10.gif")
        self.img_text = tk.Button(self.menu_frame,image = self.text_img,bg = "#177bd0",command = self.select_text,bd = 0)

        self.img_text.place(relx=0,rely=0.498,relheight=0.166,relwidth=0.15)
        self.btu_text.place(relx=0.15,rely=0.498,relheight=0.166,relwidth=0.85)

        # self.btu_text.grid(row=3)
        # 第5个功能 设置
        self.btu_set = tk.Button(self.menu_frame, text='设  置', font=('宋体', 24), anchor='c',command = self.select_set,fg = "#fff",bd = 0,bg = "#177bd0")
        # self.btu_set.configure(width=19, height=2)
        self.set_img  = tk.PhotoImage(file ="image/3_13.gif")
        self.img_set = tk.Button(self.menu_frame,image = self.set_img,command = self.select_set,bd = 0,bg = "#177bd0")

        self.btu_set.place(relx=0.15,rely=0.664,relheight=0.166,relwidth=0.85)
        self.img_set.place(relx=0,rely=0.664,relheight=0.166,relwidth=0.15)
        # self.btu_set.grid(row=4)
        # 第6个功能 关于软件
        self.btu_about = tk.Button(self.menu_frame, text='关于软件', font=('宋体', 24), anchor='c',command = self.select_about,fg = "#fff",bd = 0,bg = "#177bd0")
        # self.btu_about.configure(width=19, height=2)
        self.about_img = tk.PhotoImage(file = "image/2_08.gif")
        self.img_about = tk.Button(self.menu_frame,image = self.about_img,command = self.select_about,bd = 0,bg = "#177bd0")

        self.btu_about.place(relx=0.15,rely=0.83,relheight=0.17,relwidth=0.85)
        self.img_about.place(relx=0,rely=0.83,relheight=0.17,relwidth=0.15)
        self.select_search()
        # self.btu_about.grid(row=5)
        pass
    # 当选中在线搜词功能的时候
    def select_search(self):
        # 将选中的变为红色
        self.btu_search.configure(bg='#4595d9')
        # 其他全为黑色
        self.btu_set.configure(bg='#177bd0')
        self.btu_learn.configure(bg='#177bd0')
        self.btu_text.configure(bg='#177bd0')
        self.btu_about.configure(bg='#177bd0')
        self.btu_words.configure(bg='#177bd0')

        # 图片更换计划
        self.img_search.configure(image = self.translate_img_op,bg='#4595d9')
        # 其他的保持原状
        self.img_set.configure(image = self.set_img,bg='#177bd0')
        self.img_learn.configure(image = self.learn_image,bg='#177bd0')
        self.img_about.configure(image = self.about_img,bg='#177bd0')
        self.img_text.configure(image = self.text_img,bg='#177bd0')
        self.img_words.configure(image = self.words_img,bg='#177bd0')
        # TODO  在线搜词模块的实现
        # 每次 选择之前都要先把 frame 清空
        self.clean_Label(self.op_frame)
        # 对象可以调用成员方法 ， 但是 类不可以调用成员方法。
        serchui.search_UI(self.op_frame)
        pass
    # 同上
    def select_learn(self):
        # 从数据库中获得单词数和登录次数
        with sqlite3.connect("User.db") as coon:
            cur = coon.cursor()
            sql = "select bname from book where version =1"
            cur.execute(sql)
            bname = cur.fetchone()
            pass
        if not bname:
            self.b_name = "无"
            pass
        else:
            self.b_name = bname[0]
            pass
        if self.b_name == '无':
            tk.messagebox.showinfo("提示","请选择单词书后，再来开始你的学习之旅")
            return
            pass

        # 将选中的变为红色
        self.btu_learn.configure(bg='#4595d9')
        # 其他全为黑色
        self.btu_set.configure(bg='#177bd0')
        self.btu_search.configure(bg='#177bd0')
        self.btu_text.configure(bg='#177bd0')
        self.btu_about.configure(bg='#177bd0')
        self.btu_words.configure(bg='#177bd0')

        # 图片更换计划
        self.img_search.configure(image=self.translate_img,bg='#177bd0')
        # 其他的保持原状
        self.img_set.configure(image=self.set_img,bg='#177bd0')
        self.img_learn.configure(image=self.learn_image_op,bg='#4595d9')
        self.img_about.configure(image=self.about_img,bg='#177bd0')
        self.img_text.configure(image=self.text_img,bg='#177bd0')
        self.img_words.configure(image=self.words_img,bg='#177bd0')
        # TODO  单词学习模块的实现
        # 每次 选择之前都要先把 frame 清空
        self.clean_Label(self.op_frame)
        learnui.learn_ui(self.op_frame,self.name)
        # print(11111)
        pass

     # 同上
    def select_text(self):
        # 将选中的变为红色
        self.btu_text.configure(bg='#4595d9')
        # 其他全为黑色
        self.btu_set.configure(bg='#177bd0')
        self.btu_search.configure(bg='#177bd0')
        self.btu_learn.configure(bg='#177bd0')
        self.btu_about.configure(bg='#177bd0')
        self.btu_words.configure(bg='#177bd0')

        # 图片更换计划
        self.img_search.configure(image=self.translate_img,bg='#177bd0')
        # 其他的保持原状
        self.img_set.configure(image=self.set_img,bg='#177bd0')
        self.img_learn.configure(image=self.learn_image,bg='#177bd0')
        self.img_about.configure(image=self.about_img,bg='#177bd0')
        self.img_text.configure(image=self.text_img_op,bg='#4595d9')
        self.img_words.configure(image=self.words_img,bg='#177bd0')
        # TODO  单词测试模块的实现
        # 每次 选择之前都要先把 frame 清空
        self.clean_Label(self.op_frame)
        wordtextui.wordtext_ui(self.op_frame)
        pass
    # 同上
    def select_set(self):
        # 将选中的变为红色
        self.btu_set.configure(bg='#4595d9')
        # 其他全为黑色
        self.btu_text.configure(bg='#177bd0')
        self.btu_search.configure(bg='#177bd0')
        self.btu_learn.configure(bg='#177bd0')
        self.btu_about.configure(bg='#177bd0')
        self.btu_words.configure(bg='#177bd0')

        # 图片更换计划
        self.img_search.configure(image=self.translate_img, bg='#177bd0')
        # 其他的保持原状
        self.img_set.configure(image=self.set_img, bg='#4595d9')
        self.img_learn.configure(image=self.learn_image, bg='#177bd0')
        self.img_about.configure(image=self.about_img, bg='#177bd0')
        self.img_text.configure(image=self.text_img, bg='#177bd0')
        self.img_words.configure(image=self.words_img, bg='#177bd0')
        # TODO  设置模块的实现
        # 每次 选择之前都要先把 frame 清空
        self.clean_Label(self.op_frame)
        setui.set_UI(self.op_frame,self.root,self.name,self.label_owords)
        pass
     # 同上
    def select_about(self):
        # # 将选中的变为红色
        self.btu_about.configure(bg='#4595d9')
        # 其他全为黑色
        self.btu_text.configure(bg='#177bd0')
        self.btu_search.configure(bg='#177bd0')
        self.btu_learn.configure(bg='#177bd0')
        self.btu_set.configure(bg='#177bd0')
        self.btu_words.configure(bg='#177bd0')

        # 图片更换计划
        self.img_search.configure(image=self.translate_img, bg='#177bd0')
        # 其他的保持原状
        self.img_set.configure(image=self.set_img, bg='#177bd0')
        self.img_learn.configure(image=self.learn_image, bg='#177bd0')
        self.img_about.configure(image=self.about_img, bg='#4595d9')
        self.img_text.configure(image=self.text_img, bg='#177bd0')
        self.img_words.configure(image=self.words_img, bg='#177bd0')

        # TODO  关于软件的模块的实现
        # 每次 选择之前都要先把 frame 清空
        self.clean_Label(self.op_frame)
        # 进入 关于软件模块
        aboutui.about_UI(self.op_frame)
        pass
     # 同上
    def select_words(self):
        # 将选中的变为红色
        self.btu_words.configure(bg='#4595d9')
        # 其他全为黑色
        self.btu_text.configure(bg='#177bd0')
        self.btu_search.configure(bg='#177bd0')
        self.btu_learn.configure(bg='#177bd0')
        self.btu_set.configure(bg='#177bd0')
        self.btu_about.configure(bg='#177bd0')

        # 图片更换计划
        self.img_search.configure(image=self.translate_img,bg='#177bd0')
        # 其他的保持原状
        self.img_set.configure(image=self.set_img,bg='#177bd0')
        self.img_learn.configure(image=self.learn_image,bg='#177bd0')
        self.img_about.configure(image=self.about_img,bg='#177bd0')
        self.img_text.configure(image=self.text_img,bg='#177bd0')
        self.img_words.configure(image=self.words_img_op,bg='#4595d9')
        # TODO  单词本模块的实现
        # 每次 选择之前都要先把 frame 清空
        self.clean_Label(self.op_frame)
        if self.name !="admin":
            wordlistui.wordlist_ui(self.op_frame,self.name)
        else:
            tk.messagebox.showinfo("提示","admin账户不支持使用该功能")
            pass
        pass
    # 清空frame上的全部东西
    def clean_Label(self,frame):
        for label in frame.winfo_children():
            label.destroy()
            pass

    pass
if __name__ == '__main__':
    mainUI = main_ui()