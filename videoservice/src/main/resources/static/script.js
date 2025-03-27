function loadVideo() {
    const videoTitle = document.getElementById('videoTitle').value;
    const videoUrl = document.getElementById('videoUrl').value;
    const videoId = extractVideoId(videoUrl);
  
    if (videoId) {
      const videoPlayer = document.getElementById('videoPlayer');
      videoPlayer.src = `https://www.youtube.com/embed/${videoId}`;
  
      const videoContainer = document.getElementById('videoContainer');
      videoContainer.style.display = 'block';
  
      makeDraggable(document.getElementById('videoWindow'));
  
      // Envoyer le titre et l'URL au backend
      saveVideo(videoTitle, videoUrl);
    } else {
      alert("URL YouTube invalide. Veuillez entrer une URL valide.");
    }
  }
  
  function saveVideo(title, url) {
    const videoData = {
      title: title,
      url: url
    };
  
    fetch('/videos', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(videoData),
    })
      .then(response => response.json())
      .then(data => {
        console.log('Vidéo enregistrée avec succès:', data);
      })
      .catch(error => {
        console.error('Erreur lors de l\'enregistrement de la vidéo:', error);
      });
  }
  
  function loadVideoList() {
    fetch('/videos')
      .then(response => response.json())
      .then(videos => {
        const videoList = document.getElementById('videoList');
        videoList.innerHTML = ''; // Effacer la liste actuelle
  
        videos.forEach(video => {
          const listItem = document.createElement('li');
          listItem.textContent = video.title;
          listItem.addEventListener('click', () => playVideo(video.url));
          videoList.appendChild(listItem);
        });
  
        document.getElementById('videoListContainer').style.display = 'block';
      })
      .catch(error => {
        console.error('Erreur lors de la récupération des vidéos:', error);
      });
  }
  
  function playVideo(url) {
    const videoId = extractVideoId(url);
    if (videoId) {
      const videoPlayer = document.getElementById('videoPlayer');
      videoPlayer.src = `https://www.youtube.com/embed/${videoId}`;
  
      const videoContainer = document.getElementById('videoContainer');
      videoContainer.style.display = 'block';
    }
  }
  
  function closeVideo() {
    const videoContainer = document.getElementById('videoContainer');
    videoContainer.style.display = 'none';
    document.getElementById('videoPlayer').src = '';
  }
  
  function extractVideoId(url) {
    const regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|&v=)([^#&?]*).*/;
    const match = url.match(regExp);
    return (match && match[2].length === 11) ? match[2] : null;
  }
  
  function makeDraggable(element) {
    let isDragging = false;
    let offsetX, offsetY;
  
    element.addEventListener('mousedown', (e) => {
      isDragging = true;
      offsetX = e.clientX - element.getBoundingClientRect().left;
      offsetY = e.clientY - element.getBoundingClientRect().top;
    });
  
    document.addEventListener('mousemove', (e) => {
      if (isDragging) {
        element.style.left = `${e.clientX - offsetX}px`;
        element.style.top = `${e.clientY - offsetY}px`;
      }
    });
  
    document.addEventListener('mouseup', () => {
      isDragging = false;
    });
  }