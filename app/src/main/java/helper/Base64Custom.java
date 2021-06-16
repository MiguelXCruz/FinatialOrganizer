package helper;

import android.util.Base64;

public class Base64Custom {

   public static String codifing64Base(String text){

      return Base64.encodeToString(text.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)","");

   }

   public static String decodifing64Base(String codifidedText){
       return new String (Base64.decode(codifidedText, Base64.DEFAULT));  //fazendo com que retorne uma string, já que não retorna
   }

}
