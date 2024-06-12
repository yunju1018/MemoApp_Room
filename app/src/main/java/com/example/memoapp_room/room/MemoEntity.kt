package com.example.memoapp_room.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.memoapp_room.memo.models.MemoData
import java.io.Serializable

// 데이터베이스에 데이터 스키마(데이터베이스의 구조와 제약조건에 관한 전반적인 명세를 기술)
@Entity(tableName = "memo")
// 데이터베이스 테이블의 각 행 고유 식별 기본 키(PrimaryKey) 정의 필수 (자동 할당 autoGenerate = true)
// id 값을 null로 넣어줘도 자동으로 값이 할당된다.
data class MemoEntity(
    @PrimaryKey
    var id: String,
    var memoList: List<MemoData>
) : Serializable

/**
 * todo : PrimaryKey (날짜) 기준 ViewPager
 * memo List typeConverter 사용 리스트 만들기 (hashmap 되나?? 확인)
 */

