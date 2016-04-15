package util


class FileUtil {

    def static write(String file,String text){
        def fileObject = new File(file)
        fileObject << text
    }
}
