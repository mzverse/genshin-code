package mz.genshincode.shit;

/**
 * 图注释
 * 表示节点图中的注释或说明文本
 */
public class GraphComment {
    private final String content;
    private final float x;
    private final float y;

    public GraphComment(String content, float x, float y) {
        this.content = content;
        this.x = x;
        this.y = y;
    }

    public String getContent() {
        return content;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public String toString() {
        return "GraphComment{" +
                "content='" + content + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}