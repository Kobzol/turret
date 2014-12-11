package cz.kobzol.turret.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.graphics.DrawableShape;
import cz.kobzol.turret.graphics.SpriteObject;
import cz.kobzol.turret.model.GameObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;

/**
 * Loads maps and objects from XML files.
 */
public class ObjectLoader {
    private static String CLASS_NAMESPACE = "cz.kobzol.turret";

    private AssetManager assetManager;

    public ObjectLoader(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public ObjectManager parseObjectManager(FileHandle levelXML) {
        return this.parseObjectManager(levelXML.read());
    }

    public ObjectManager parseObjectManager(String levelXML) {
        return this.parseObjectManager(new ByteArrayInputStream(levelXML.getBytes()));
    }

    /**
     * Parses level from the given XML.
     * @param inputXML inputstream with XML
     * @return parsed ObjectManager or null in case of failure
     */
    public ObjectManager parseObjectManager(InputStream inputXML) {
        ObjectManager level = new ObjectManager();
        Document dom = this.createDOM(inputXML);

        NodeList nodes = dom.getDocumentElement().getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (node instanceof Element) {
                Element element = (Element) node;

                if (element.getTagName().equals("objects")) {
                    this.parseObjects(level, element);
                }
            }
        }

        return level;
    }

    /**
     * Parses objects from DOM element. Expects child nodes with tag object.
     * @param level level
     * @param elementObjects DOM element
     */
    private void parseObjects(ObjectManager level, Element elementObjects) {
        for (int i = 0; i < elementObjects.getChildNodes().getLength(); i++) {
            Node node = elementObjects.getChildNodes().item(i);

            if (node instanceof Element) {
                Element element = (Element) node;

                if (element.getTagName().equals("object")) {
                    GameObject object = (GameObject) this.createObject(element);

                    if (element.hasAttribute("key")) {
                        object.setKey(element.getAttribute("key"));
                    }

                    for (String iface : element.getAttribute("interfaces").split(",")) {
                        if (iface.equals("graphics.SpriteObject")) {
                            this.parseSpriteObject(object, element);
                        }
                    }

                    level.registerObject(object);
                }
            }
        }
    }

    /**
     * Parses an object from the given DOM element. Expects attribute class.
     * @param elementObject DOM element
     * @return parsed Object or null if the instantiation was unsuccessful
     */
    private Object createObject(Element elementObject) {
        try {
            Class<?> objectClass = Class.forName(ObjectLoader.CLASS_NAMESPACE + "." + elementObject.getAttribute("class"));
            Constructor<?> objectConstructor = objectClass.getConstructor();
            return objectConstructor.newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * Parses shape from DOM element. Expects child nodes dimension and location.
     * @param object parsed object
     * @param elementObject DOM element
     */
    private void parseShape(Object object, Element elementObject) {
        DrawableShape shape = (DrawableShape) object;

        Vector2 position = this.parsePosition(elementObject);
        Texture texture = this.parseTexture(elementObject);
        Dimension dimension = this.parseDimension(elementObject);

        if (position != null) {
            shape.setPosition(position);
        }

        if (texture != null) {
            shape.setTexture(texture);
        }

        if (dimension != null) {
            shape.setDimension(dimension);
        }
    }

    /**
     * Parses SpriteObject.
     * @param object GameObject
     * @param elementObject DOM element
     */
    private void parseSpriteObject(Object object, Element elementObject) {
        SpriteObject spriteObject = (SpriteObject) object;

        this.parseShape(object, elementObject);

        spriteObject.setSpeed(this.parseSpeed(elementObject));
        spriteObject.setRotation(this.parseRotation(elementObject));
    }

    /**
     * Parses Texture from DOM element. Expects attribute value (the location of the texture).
     * @param element DOM element
     * @return texture
     */
    private Texture parseTexture(Element element) {
        element = this.getElementOrChild(element, "texture");

        if (element == null) {
            return null;
        }

        return this.loadAsset(element.getAttribute("value"), Texture.class);
    }

    /**
     * Parses Vector2 (position) from DOM element. Expects double attributes x and y.
     * @param element DOM element
     * @return vector2 (position)
     */
    private Vector2 parsePosition(Element element) {
        element = this.getElementOrChild(element, "position");

        if (element == null) {
            return null;
        }

        return new Vector2(Float.parseFloat(element.getAttribute("x")), Float.parseFloat(element.getAttribute("y")));
    }

    /**
     * Parses Dimension from DOM element. Expects double attributes width and height.
     * @param element DOM element
     * @return dimension
     */
    private Dimension parseDimension(Element element) {
        element = this.getElementOrChild(element, "dimension");

        if (element == null) {
            return null;
        }

        Dimension dimension = new Dimension();
        dimension.setSize(Double.parseDouble(element.getAttribute("width")), Double.parseDouble(element.getAttribute("height")));

        return dimension;
    }

    /**
     * Parses speed from the DOM element.
     * @param element DOM element
     * @return speed or 0 if the element did not contain speed
     */
    private float parseSpeed(Element element) {
        element = this.getElementOrChild(element, "speed");

        if (element == null) {
            return 0.0f;
        }

        return Float.parseFloat(element.getAttribute("value"));
    }

    /**
     * Parses rotation in degrees from DOM element.
     * @param element DOM element
     * @return rotation in degrees or 0 if the element did not contain rotation
     */
    private float parseRotation(Element element) {
        element = this.getElementOrChild(element, "rotation");

        if (element == null) {
            return 0.0f;
        }

        return Float.parseFloat(element.getAttribute("value"));
    }

    /**
     * Creates DOM from the given string.
     * @param input input string
     * @return DOM created from the string or null if the DOM build failed
     */
    private Document createDOM(InputStream input) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document dom = db.parse(input);
            dom.normalizeDocument();

            return dom;
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Get the element if it's tag matches the given tag or try to find his child with given tag.
     * @param element element
     * @param tagName tag name
     * @return given element, his child or null if no tag with the given tag name was found
     */
    private Element getElementOrChild(Element element, String tagName) {
        Element foundElement = null;

        if (!element.getTagName().equals(tagName)) {
            Element childElement = this.findElementChild(element, tagName);

            if (childElement != null) {
                foundElement = childElement;
            }
        }
        else foundElement = element;

        return foundElement;
    }

    /**
     * Find child element of the given element with the given tag name.
     * @param parentElement parent element
     * @param tagName tag name of child element
     * @return found child element or null
     */
    private Element findElementChild(Element parentElement, String tagName) {
        for (int i = 0; i < parentElement.getChildNodes().getLength(); i++) {
            Node node = parentElement.getChildNodes().item(i);

            if (node instanceof Element && ((Element) node).getTagName().equals(tagName)) {
                return (Element) node;
            }
        }

        return null;
    }

    private <T> T loadAsset(String assetName, Class<T> cls) {
        if (this.assetManager != null) {
            return this.assetManager.get(assetName, cls);
        }
        else return null;
    }
}
