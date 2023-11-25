package hr.algebra.carcassonnegame2.utils;

import java.io.*;


public final class DocumentationUtils {

    private static final String LAUNCHER_CLASS ="Main.java";
    private static final String MAIN_FILE_NAME ="index";
    private static final String MAIN_FOLDER_NAME = "project-documentation";
    private static final String DOCUMENTATION_TITLE = "Carcassonne Game Documentation";
    private static final String PROJECT_NAME ="src/main/java/hr/algebra/carcassonnegame2/";
    private static final String ICON_ROUTE = "src/main/resources/hr/algebra/carcassonnegame2/images/icon.ico";
    private static final String HEADERS = "<head>\n" +
            "    <link rel=\"icon\" href=\"%s\" type=\"image/x-icon\">\n" +
            "    <title>%s</title>\n" +
            "</head>";
    private static final String PACKAGE_FILE_CONVENTION = "Package";

    public static void generateDocumentation() throws IllegalArgumentException{
        File project = new File(PROJECT_NAME);
        createFolder(MAIN_FOLDER_NAME);
        StringBuilder sb = new StringBuilder();
        initContent(sb, DOCUMENTATION_TITLE, "../"+ICON_ROUTE);
        generateDocumentationAux(project, new StringBuilder(MAIN_FOLDER_NAME), sb, MAIN_FILE_NAME +".html");
        closeContent(sb, new StringBuilder(MAIN_FOLDER_NAME), MAIN_FILE_NAME, false);
    }

    private static void createFolder(String folderName) {
        File folder = new File(folderName);
        if(!folder.exists()){
            folder.mkdir();
        }
    }
    private static void generateDocumentationAux(File project, StringBuilder currentFolder, StringBuilder content, String parent) throws IllegalArgumentException{
        if(project.isDirectory()){
            createFolder(currentFolder.toString());
            File[] projectFiles = project.listFiles();
            if(projectFiles!=null) {
                for (File file : projectFiles) {
                    if(!file.getName().equals(LAUNCHER_CLASS)) {
                        String htmlName = getFileNameConvention(file);
                        StringBuilder newCurrentFolder = new StringBuilder(currentFolder);
                        StringBuilder newContent = new StringBuilder();
                        initContent(newContent, getTitleNameConvention(file.getName()), generateIconRoute(currentFolder));
                        addGoBackLink(newContent, parent, !file.isDirectory());
                        if (file.isDirectory()) {
                            addFolderToContent(content, htmlName, currentFolder);
                            generateDocumentationAux(file, newCurrentFolder.append(File.separator).append(file.getName()), newContent, htmlName+PACKAGE_FILE_CONVENTION+".html");
                        } else {
                            addFileToContent(content, htmlName, currentFolder);
                            addLineCounter(file, newContent);
                            generateDocumentationForNotDir(file, newCurrentFolder, newContent);
                        }
                        closeContent(newContent, new StringBuilder(currentFolder + (file.isDirectory() ? File.separator + htmlName : "")), htmlName, file.isDirectory());
                    }
                }
            }
        }
    }

    private static void addLineCounter(File file, StringBuilder newContent) {
        newContent.append("<h4>Number of lines: <em>").append(countLinesOfFile(file)).append("</em></h4>");
    }

    private static int countLinesOfFile(File file){
        try {
            BufferedReader lector = new BufferedReader(new FileReader(file));
            int counter = 0;

            while (lector.readLine() != null) {
                counter++;
            }

            lector.close();
            return counter;
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read the file");
        }
    }

    private static String getFileNameConvention(File file){
        String name = file.isDirectory()? file.getName():getJavaFileName(file.getName());
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }


    private static void initContent(StringBuilder sb, String title, String iconRoute) {
        String[] aux = title.split(" ");
        String pageName = aux[aux.length-1];
        sb.append("<html>").append(HEADERS.formatted(iconRoute, pageName)).append("<h1>").append(title).append("</h1>");
    }

    private static void closeContent(StringBuilder sb, StringBuilder url, String fileName, boolean isPackage) {
        sb.append("</html>");
        String aux = url + File.separator + fileName + (isPackage? PACKAGE_FILE_CONVENTION:"") + ".html";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(aux))) {
            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addGoBackLink(StringBuilder content, String parent, boolean isFile) {
        if(parent != null){
            content.append("<a href=\"").append(!isFile? ".."+File.separator:"").append(parent).append("\">").append("../").append("</a><br>");
        }
    }

    private static String getTitleNameConvention(String fileName){
        return Character.toUpperCase(fileName.charAt(0)) + fileName.substring(1);
    }

    private static String getJavaFileName(String fileName) {
        return fileName.substring(0, fileName.length() - 5);
    }

    private static String generateIconRoute(StringBuilder currentFolder) {
        String f = File.separator;
        String filename = currentFolder.toString().replace("\\", "/");
        String[] splitFileName = filename.split("/");

        StringBuilder end = new StringBuilder();
        for (String st: splitFileName){
            end.append("../");
        }

        return end + ICON_ROUTE;
    }

    private static void addFolderToContent(StringBuilder content, String fileName, StringBuilder url) {
        content.append("<a href=\"").append(fileName).append(File.separator).append(fileName).append(PACKAGE_FILE_CONVENTION).append(".html\">").append("\uD83D\uDDC0:   ").append(getTitleNameConvention(fileName)).append("</a><br>");
    }

    private static void addFileToContent(StringBuilder content, String fileName, StringBuilder currentFolder) {
        content.append("<a href=\"").append(fileName).append(".html\">").append("\uD83D\uDDCB:   ").append(getTitleNameConvention(fileName)).append(".java</a><br>");
    }

    private static void generateDocumentationForNotDir(File file, StringBuilder currentFolder, StringBuilder newContent) throws IllegalArgumentException{
        String name = getJavaFileName(file.getPath().substring("src\\main\\java\\".length()).replace("\\", "."));
        Class<?> clazz = getClassForFile(name, currentFolder);
        ReflectionUtils.readClassAndMembersInfo(clazz, newContent);
    }

    private static Class<?> getClassForFile(String fileName, StringBuilder currentFolder) throws IllegalArgumentException{
        try {
            return Class.forName(fileName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
