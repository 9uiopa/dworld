// 삭제 기능
// eventListener에서 람다식 한번, then() 에서 람다식 한번
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('article-id').value;
        fetch(`/api/articles/${id}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok){
                    console.log("deleted completely")
                    alert('삭제가 완료되었습니다.');
                }

                // url 이동 : localhost:8080/articles
                location.replace('/articles');
            });
    });
}


// 수정 기능
const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        fetch(`/api/articles/${id}`, {
            method: 'PUT',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                title: document.getElementById('title').value,
                content: document.getElementById('content').value
            })
        })
            .then(() => {
                alert('수정이 완료되었습니다.');
                location.replace(`/articles/${id}`);
            })
    });
}

// 생성 기능
const createButton = document.getElementById('create-btn');

if (createButton) {
    createButton.addEventListener('click', event => {
        let boardTypeId = document.getElementById('boardType-id').value
        console.log("boardTypeId : " + boardTypeId);

        fetch('/api/articles', {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                title: document.getElementById('title').value,
                content: document.getElementById('content').value,
                author: document.getElementById('article-author').value,
                boardTypeId: boardTypeId
            })
        })
            .then(() => {
                alert('등록 완료되었습니다.');
                location.replace(`/articles?boardType=${boardTypeId}`);
            })
            .catch(error => {
                console.error('There has been a problem with your fetch operation:', error);
                alert('등록에 실패했습니다. 다시 시도해주세요.');
                });
    });
}