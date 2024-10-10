package com.sparta.memo.Controller;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class MemoController {

    // database 대신 자바 컬렉션 중 Map 자료구조 사용
    private final Map<Long, Memo> memoList = new HashMap<>();

    //Create 구현
    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto){
        // RequestDto -> Entity
        Memo memo = new Memo(requestDto);

        // Memo Max ID Check
        Long maxId = memoList.size() > 0 ? Collections.max(memoList.keySet()) + 1 : 1;
        memo.setId(maxId);

        // DB 저장
        memoList.put(memo.getId(), memo);

        // Entity -> ResponseDto
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);

        return memoResponseDto;
    }

    // Read 구현
    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos(){
        // Map to List, 위에선 Map으로 구현했지만 List로도 구현할 수 있어야 함
        List<MemoResponseDto> responseList = memoList.values().stream().map(MemoResponseDto::new).toList();
        return responseList;
    }

    // Update 구현
    @PutMapping("/memos/{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto){
        // 해당 메모가 DB에 존재하는지 확인
        if(memoList.containsKey(id)){
            // True
            // 해당 메모 가져오기
            Memo memo = memoList.get(id);

            // 메모 수정
            memo.update(requestDto);
            return memo.getId();
        } else {
            throw new IllegalArgumentException("Memo not found");
        }
    }

    // Delete 구현
    @DeleteMapping("/memos/{id}")
    public Long deleteMemo(@PathVariable Long id){
        if(memoList.containsKey(id)){
            // True - 해당 메모 삭제하기
            memoList.remove(id);
            return id;
        } else {
            throw new IllegalArgumentException("Memo not found");
        }
    }
}
