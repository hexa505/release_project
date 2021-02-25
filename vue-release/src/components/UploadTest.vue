<template>
  <div>
    <legend><h1>Album</h1></legend>
    <form action="upload" method="post" enctype="multipart/form-data">
    <div id="albumform">
        <label for="file-input">
    <img src="https://icon-library.net/images/upload-photo-icon/upload-photo-icon-21.jpg"/>
  </label><br/>
  <input id="file-input" type="file" value="앨범표지" name="album" style="display: none"/>
      title : <input type="text" name="title" /><br />
      description : <textarea v-model="description" placeholder="description 작성..."></textarea><br/>
      tag : <input type="text" name="tag" /><br />
      </div>
      <legend><h1>Photos</h1></legend>
      <ol><div id="photoList">
        {{ num }}
      <div id="photoform">
        <li>
               <label for="file-input">
    <img src="https://icon-library.net/images/upload-photo-icon/upload-photo-icon-21.jpg"/>
  </label><br/>
  <input id="file-input" type="file" value="사진" name="photo" style="display: none"/>
        title : <input type="text" name="title" /><br />
        description : <textarea v-model="description" placeholder="description 작성..."></textarea><br/>
        tag : <input type="text" name="tag" />
        <remover></remover>
            <button type="button" v-on:click="addPhotoList">Add</button><br />
            <div id="components-demo">
  <button-counter></button-counter>
</div>
        </li>
      </div>
      </div>
      </ol>
      <button>submit</button>
    </form>
  </div>
</template>

<script>
export default {
  name: 'UploadTest',
  props: {
    label: {
      required: false,
      type: String
    },
    done: {
      default: false,
      type: Boolean
    }
  },
  methods: {
    addPhotoList: function (event) {
      var addFormDiv = document.getElementById('photoform')
      var photoForm = '<li><label for="file-input"><img src="https://icon-library.net/images/upload-photo-icon/upload-photo-icon-21.jpg"/></label><br/><input id="file-input" type="file" value="사진" name="photo" style="display: none"/>title : <input type="text" name="title" /><br />description : <textarea v-model="description" placeholder="description 작성..."></textarea><br />tag : <input type="text" name="tag" /></li>'
      var addDiv = document.createElement('div')
      addDiv.setAttribute('id', 'photoList')
      addDiv.innerHTML = photoForm
      addFormDiv.appendChild(addDiv)
    },
    removePhotoList: function (event) {
      var removeDiv = document.getElementById('photoform')
      var parent = document.getElementById(removeDiv)
      parent.removeChild(removeDiv)
    }
  }
}
</script>
