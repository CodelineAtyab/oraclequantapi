package com.oraclequantapi.oraclequantapi.repositories;

import com.oraclequantapi.oraclequantapi.models.Sequence;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SequenceRepository {

    private final List<Sequence> sequenceList = new ArrayList<>();

    public boolean saveSequence(Sequence sequence){
        if (sequence == null || !sequence.isValid())
            return false;
        sequenceList.add(sequence);
        return true;
    }

    //return all stored sequence obj
    public List<Sequence> getAll(){
        return new ArrayList<>(sequenceList);
    }

    //clear in-memory list
    public void clear(){
        sequenceList.clear();
    }
}
