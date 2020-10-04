package coursework.InderPanesar;

import com.sun.prism.paint.Color;

import GraphicsLab.Vertex;

public class Button {
	
	public Vertex vertex1, vertex2, vertex3, vertex4, vertex5, vertex6, vertex7, vertex8;
	
	public float MinimumZ;
	public float MaximumZ;
	
	public float translationX;
	
	
	public Button(Vertex vertex1, Vertex vertex2, Vertex vertex3, Vertex vertex4, Vertex vertex5, Vertex vertex6, Vertex vertex7, Vertex vertex8) {
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
		this.vertex3 = vertex3;
		this.vertex4 = vertex4;
		this.vertex5 = vertex5;
		this.vertex6 = vertex6;
		this.vertex7 = vertex7;
		this.vertex8 = vertex8;
		translationX = 0;
		MinimumZ = 0;
		MaximumZ = 0;
		
		translationX = 0;
	}
	
	public void setTranslation(float X, float Z) {
		MaximumZ = Z - 1f;
		MinimumZ = Z + 1f;
		translationX = X-0.5f;

	}
	
	public Boolean checkCollisions(float valueX, float valueZ) {
		if(vertex3.getX()+translationX >= valueX && vertex2.getX()+translationX <= valueX && MinimumZ > valueZ && MaximumZ < valueZ) {
			return true;
		}

		return false;
	}
	
	
	public Color isHit() {
		return Color.RED;
	}
	


}
