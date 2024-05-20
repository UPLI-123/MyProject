# -*- mode: python ; coding: utf-8 -*-

block_cipher = None


a = Analysis(['loginui.py'],
             pathex=['aboutui.py', 'learnui.py', 'mainui.py', 'serchui.py', 'wordlistui.py', 'wordtextui.py', 'ydtranslate.py', 'C:\\编程空间\\py_workspace\\大学英语系统'],
             binaries=[],
             datas=[],
             hiddenimports=[],
             hookspath=[],
             runtime_hooks=[],
             excludes=[],
             win_no_prefer_redirects=False,
             win_private_assemblies=False,
             cipher=block_cipher,
             noarchive=False)
pyz = PYZ(a.pure, a.zipped_data,
             cipher=block_cipher)
exe = EXE(pyz,
          a.scripts,
          a.binaries,
          a.zipfiles,
          a.datas,
          [],
          name='loginui',
          debug=False,
          bootloader_ignore_signals=False,
          strip=False,
          upx=True,
          upx_exclude=[],
          runtime_tmpdir=None,
          console=False , icon='English.ico')
