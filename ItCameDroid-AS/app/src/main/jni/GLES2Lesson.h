//
// Created by monty on 23/11/15.
//

#ifndef LESSON02_GLES2LESSON_H
#define LESSON02_GLES2LESSON_H

namespace odb {

	enum EFadeState {
		kNormal,
		kFadingIn,
		kFadingOut
	};

	enum ETextures {
		None,
		Floor,
		Wall,
		Ceiling,
		Hero,
		Monters,
		Explosion
	};

	using IntGameMap = std::array<std::array<ETextures , 50>, 50>;
	using IntField = std::array<std::array<int, 50>, 50>;
	using LightMap = IntField;
	using AnimationList = std::map< int, std::tuple<glm::vec2, glm::vec2, long> >;
	static const long kAnimationLength = 500;

	class GLES2Lesson {

		void fetchShaderLocations();

		void setPerspective();

		void prepareShaderProgram();

		void clearBuffers();

		void resetTransformMatrices();

		void printVerboseDriverInformation();

		void createVBOs();

		void deleteVBOs();

		void drawGeometry(const int vertexVbo, const int indexVbo, int vertexCount,
		                  const glm::mat4 &transform);

		GLuint createProgram(const char *pVertexSource, const char *pFragmentSource);

		GLuint loadShader(GLenum shaderType, const char *pSource);

		const static float cubeVertices[16 * 5];
		const static unsigned short cubeIndices[6 * 4];

		glm::mat4 cubeTransformMatrix;
		glm::mat4 projectionMatrix;

		GLint vertexAttributePosition;
		GLint modelMatrixAttributePosition;
		GLint samplerUniformPosition;
		GLint textureCoordinatesAttributePosition;
		GLint projectionMatrixAttributePosition;
		GLuint gProgram;
		GLuint uView;
		GLuint uMod;
		//VBO stuff
		GLuint vboCubeVertexDataIndex;
		GLuint vboCubeVertexIndicesIndex;

		GLuint vboBillboardVertexDataIndex;
		GLuint vboBillboardVertexIndicesIndex;

		GLuint vboFloorVertexDataIndex;
		GLuint vboFloorVertexIndicesIndex;

		GLint fadeUniform;


		std::vector<std::shared_ptr<NativeBitmap>> mBitmaps;
		std::vector<std::shared_ptr<Texture>> mTextures;

		bool mCloseUpCamera = true;
		glm::vec2 mCameraTarget;


		glm::vec3 mClearColour;
		glm::vec4 mFadeColour = glm::vec4( 0.0f, 0.0f, 0.0f, 1.0f );
		EFadeState mFadeState = EFadeState::kNormal;
	public:
		GLES2Lesson();

		~GLES2Lesson();

		bool init(float w, float h, const std::string &vertexShader,
		          const std::string &fragmentShader);

		void setTexture(std::vector<std::shared_ptr<NativeBitmap>> textures);

		void render(IntGameMap map, IntGameMap actors, IntGameMap splats, LightMap lightmap, IntField ids, AnimationList movingCharacters, long animationTime);

		void shutdown();

		void toggleCloseUpCamera();

		void setCameraPosition(float x, float y);

		void setCursorAt( float x, float y );

		void setClearColour( float r, float g, float b );

		void startFadingIn();

		void startFadingOut();

		void updateCamera( long ms );

		glm::vec2 cameraPosition;
		glm::vec2 cursorPosition;
		static const float billboardVertices[20];
		static const unsigned short billboardIndices[6];
		static const float floorVertices[20];
		static const unsigned short floorIndices[6];

		void updateFadeState(long ms);
	};
}
#endif //LESSON02_GLES2LESSON_H