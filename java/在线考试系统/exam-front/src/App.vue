<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<style>
  <!-- 设置全局的样式-->
  ul {
    list-style: none;
  }
  a {
    text-decoration: none;
  }

  * {
    margin: 0;
    padding: 0;
  }
  /* 设置字体颜色和字体样式*/
  #app {
    font-family: "Microsoft YaHei", "Helvetica", "Tahoma", "Geneva", "Arial", sans-serif;
    background-color: #eee;
  }

</style>
