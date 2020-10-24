
; main
main:
	call fib_8_i
	
halt_loop:
	jmp halt_loop

; 8 bit fibonacci
fib_8_i:
	push 1		; 1
	push 0		; 1, 0

fib_8:			; n-1, n-2
	dupo1		; n-1, n-2, n-1
	add		; n-1, n
	dup		; n-1, n, n
	call print_byte	; n-1, n
	swap		; n, n-1
	dup2		; n, n-1, n, n-1
	ucl		; n, n-1
	jmpc fib_8
	ret


; 16 bit fibonacci
fib_16_i:
	pushw 1		; 0, 1
	pushw 0		; 0, 1, 0, 0

fib_16:			; n-1_h, n-1_l, n-2_h, n-2_l
	dupwo2		; n-1_h, n-1_l, n-2_h, n-2_l, n-1_h, n-1_l
	addw		; n-1_h, n-1_l, n_h, n_l
	dupw		; n-1_h, n-1_l, n_h, n_l, n_h, n_l
	call print_word ; n-1_h, n-1_l, n_h, n_l
	swapw		; n_h, n_l, n-1_h, n-1_l
	dupwo2		; n_h, n_l, n-1_h, n-1_l, n_h, n_l
	dupwo2		; n_h, n_l, n-1_h, n-1_l, n_h, n_l, n-1_h, n-1_l
	uclw		; n_h, n_l, n-1_h, n-1_l
	jmpc fib_16
	ret


print_word:			; n_h, n_l
	swap			; n_l, n_h
	call print_byte		; n_l
	call print_byte		; 
	ret

print_byte:			; n
	dup			; n, n
	and 0xF0		; n, masked
	times 4 srl		; n, high
	call print_nybble	; n
	and 0x0F		; low
	call print_nybble	;
	ret

print_nybble:		; n
	dup		; n, n
	ucge 0x0A	; n
	jmpc .high
.low:			; n
	add 0x30	; c
	write		;
	ret
.high:			; n
	add 0x41	; c
	write		;
	ret