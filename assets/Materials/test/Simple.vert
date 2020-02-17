varying vec2 texCoord1;// 用于传递给片元着色器的顶点纹理数据

void main(){
    projPosition = worldViewProjectionMatrix * vec4(modelPosition, 1.0);
}