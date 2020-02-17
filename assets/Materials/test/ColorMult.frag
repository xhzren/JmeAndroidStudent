varying vec2 texCoord1;
void main() {
    color1 = texture2D(colorMap, texCoord1);
    outColor = color1;
}