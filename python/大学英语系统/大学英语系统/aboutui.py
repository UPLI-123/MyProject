import tkinter as tk


class about_UI():
    def __init__(self,root):
        self.root = root
        self.display()
        pass
    # TODO 关于软件的布局
    def method_about(self):
        # 首先先清空画布
        self.clean_Lable(self.buttom_frame)
        # 一个text 框架
        self.text_about = tk.Text(self.buttom_frame,font =('宋体',24,'bold','italic'))
        self.text_about.place(relx=0.0, rely=0, relheight=1, relwidth=1)

        self.text_about.insert(tk.END,"软件名称：大学英语学习软件V2.0\n"+\
                               "制作人：LCH\n"+\
                               "制作周期：15天\n"+\
                               "完成时间：2020/12/23\n"+\
                               "布局参考：有道、金山词霸、扇贝英语\n"+\
                               "较上个版本的变动：新增了更换头像功能、美化了在线翻译、单词本、单词学习的界面")
        #

        pass
    # todo 软件操作布局
    def method_op(self):
        self.clean_Lable(self.buttom_frame)
        # 一个text 框架
        self.text_about = tk.Text(self.buttom_frame, font=('宋体', 24, 'bold', 'italic'))
        self.text_about.place(relx=0.0, rely=0, relheight=1, relwidth=1)
        self.text_about.insert(tk.END, "单词学习中的快捷键\n")
        self.text_about.insert(tk.END, "a:前一页\n")
        self.text_about.insert(tk.END, "d：后一页\n")
        self.text_about.insert(tk.END, "s：我不知道\n")
        self.text_about.insert(tk.END, "w:临摹练习\n")
        self.text_about.insert(tk.END, "z：加入单词本\n")
        self.text_about.insert(tk.END, "e：发音\n")

        pass
    # todo 注意事项布局
    def method_heed(self):
        # 首先先清空画布
        self.clean_Lable(self.buttom_frame)
        # 一个text 框架
        self.text_about = tk.Text(self.buttom_frame, font=('宋体', 24, 'bold', 'italic'))
        self.text_about.place(relx=0.0, rely=0, relheight=1, relwidth=1)

        self.text_about.insert(tk.END, "1.所导入的单词本一定要符合格式，否则会导致软件奔溃\n")
        self.text_about.insert(tk.END,"文件的格式：最新版的Excel、A1 B1单元格要是单词和翻译,下面依次填入要导入单词\n")
        self.text_about.insert(tk.END, "2.用户名和密码要符合规范\n")
        self.text_about.insert(tk.END, "用户名：3-10位任意字符\n")
        self.text_about.insert(tk.END, "密码：6-18位任意字符\n")
        self.text_about.insert(tk.END, "3.程序所在的文件件不要轻易进行修改\n")


        pass
    # todo 软件建议
    def method_advice(self):
        # 首先先清空画布
        self.clean_Lable(self.buttom_frame)
        # 一个text 框架
        self.text_about = tk.Text(self.buttom_frame, font=('宋体', 24, 'bold', 'italic'))
        self.text_about.place(relx=0.0, rely=0, relheight=1, relwidth=1)

        self.text_about.insert(tk.END, "小记：对于未来的话，有可能会增加类似于单词打开和一些基于pygame实现有关英语单词的小游戏。\n")
        self.text_about.insert(tk.END,"总结：功能比较完善，但还是存在一些小的问题，由于时间问题来不及修改了")

        pass

    # todo 清空布局操作
    def clean_Lable(self,frame):
        for label in frame.winfo_children():
            label.destroy()
            pass
        pass
    def display(self):
        # 整体上分为上下两个布局
        self.top_frame = tk.Frame(self.root,bg='red')
        self.top_frame.place(relx = 0 ,rely = 0 ,relheight = 0.22 , relwidth = 1)
        # 上组件上添加一些选择性的功能
        # 软件简介
        self.but_about_software  = tk.Button(self.top_frame,text = '软件简介',font = ('宋体',24),command = self.method_about)
        self.but_about_software.place(relx = 0 ,rely = 0 ,relheight = 1 , relwidth = 0.25)

        self.but_about_software_op = tk.Button(self.top_frame, text='软件操作', font=('宋体', 24),command =self.method_op)
        self.but_about_software_op.place(relx=0.25, rely=0, relheight=1, relwidth=0.25)

        self.but_about_software_an = tk.Button(self.top_frame, text='注意事项', font=('宋体', 24),command = self.method_heed)
        self.but_about_software_an.place(relx=0.5, rely=0, relheight=1, relwidth=0.25)

        self.but_about_software_remark = tk.Button(self.top_frame, text='软件展望', font=('宋体', 24),command = self.method_advice)
        self.but_about_software_remark.place(relx=0.75, rely=0, relheight=1, relwidth=0.25)

        # 下部分组件
        self.buttom_frame = tk.Frame(self.root,bg = 'blue')
        self.buttom_frame.place(relx = 0,rely = 0.22 ,relwidth = 1 ,relheight = 0.78)
        self.method_about()
        pass
    pass