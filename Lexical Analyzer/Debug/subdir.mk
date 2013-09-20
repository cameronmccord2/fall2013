################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../Input.cpp \
../Lex.cpp \
../State.cpp \
../Token.cpp \
../TokenType.cpp \
../Utils.cpp 

OBJS += \
./Input.o \
./Lex.o \
./State.o \
./Token.o \
./TokenType.o \
./Utils.o 

CPP_DEPS += \
./Input.d \
./Lex.d \
./State.d \
./Token.d \
./TokenType.d \
./Utils.d 


# Each subdirectory must supply rules for building sources it contributes
%.o: ../%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


