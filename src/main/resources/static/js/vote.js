const upvoteBtn = document.getElementById('upvote-btn');
const downvoteBtn = document.getElementById('downvote-btn');
const articleId = document.getElementById('article-id').value;

function vote(voteType) {
    return function() {
        fetch(`/api/articles/${articleId}/vote`, {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                voteType: voteType,
            })
        })
            .then(response => {
                switch (response.status){
                    case 201:
                        // 성공적인 응답 처리
                        if (voteType==='UPVOTE'){
                            countUpvotes();
                            alert('추천되었습니다.');
                        }else{
                            countDownvotes();
                            alert('비추천되었습니다.');
                        }
                        location.reload();
                        return;
                    case 401:
                        // 인증되지 않은 사용자
                        alert('로그인 후에만 추천 가능합니다.');
                        return;
                    case 403:
                        // 이미 추천
                        alert('이미 추천/비추천 하셨습니다.')
                        return;
                    case 500:
                        //서버 에러
                        alert('추천/비추천할 수 없습니다. 500 에러')
                        return;
                    default:
                        alert('추천/비추천할 수 없습니다.알 수 없는 에러')
                        return;
                }
            })
            .catch(error => {
                // 네트워크 오류 처리
                console.error('Error during fetch:', error);
                alert('네트워크 에러');
            });
    }
}

// 이벤트 리스너 설정
upvoteBtn.addEventListener('click', vote('UPVOTE'));
downvoteBtn.addEventListener('click', vote('DOWNVOTE'));

const upvoteCnt =  document.getElementById('upvote-count');
const downvoteCnt =  document.getElementById('downvote-count');

function countUpvotes(){
    fetch(`/api/articles/${articleId}/upvotes`,{
        method: 'GET'
    })
        .then(response => response.text())  // 응답 본문을 텍스트로 변환
        .then(data =>{
            upvoteCnt.textContent = data;
        })
        .catch(error => {
            console.error('Error:', error); // 에러 처리
        });
}

function countDownvotes(){
    fetch(`/api/articles/${articleId}/downvotes`,{
        method: 'GET'
    })
        .then(response => response.text())  // 응답 본문을 텍스트로 변환
        .then(data =>{
            downvoteCnt.textContent = data;
        })
        .catch(error => {
            console.error('Error:', error); // 에러 처리
        });
}