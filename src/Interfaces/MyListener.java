package Interfaces;

import java.io.FileNotFoundException;
import Models.Apartment;

public interface MyListener
{
    void onClickListener(Apartment apartment) throws FileNotFoundException;
}