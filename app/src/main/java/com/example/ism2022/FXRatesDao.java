package com.example.ism2022;
import java.util.List;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface FXRatesDao {
    @Insert
    long insert(FXRate fxRate);
    @Query("select * from fxrates")
    List<FXRate> getAll();
    @Query("delete from fxrates")
    void deleteAll();
    @Delete
    void delete(FXRate fxRate);

}
