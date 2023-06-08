package com.example.totaldemo.mapper;


import com.example.totaldemo.pojo.entity.BookEntity;
import com.example.totaldemo.pojo.entity.BookTestRabbit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TestPlayMapper {

    /**
     * 检查某本书的库存
     * @return
     */
    int selectInventory(@Param("id") Integer bookId);

    /**
     * 查询所有图书信息
     * @return
     */
    List<BookTestRabbit> selectAllBook();


    /**
     * 购买书籍(减库存,增加销量)
     */
    int buyBook(@Param("id") Integer bookId);

    /**
     * 添加图书
     */
    int insertBook(BookTestRabbit bookTestRabbit);

    /**
     * 根据id查看指定书籍(Rabbit)
     */
    BookTestRabbit selectBookById(@Param("id") Integer bookId);

    /**
     * 根据id查看指定书籍
     */
    BookEntity selectBookByIdEntity(@Param("id") Integer bookId);
}
