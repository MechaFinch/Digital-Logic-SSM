

; 8 bit fibonacci
main:
	push 1
	push 0

fib_8:			; n-1, n-2
	dupo1		; n-1, n-2, n-1
	add		; n-1, n
	dup		; n-1, n, n
	call print_byte	; n-1, n
	swap		; n, n-1
	dup2		; n, n-1, n, n-1
	ucl		; n, n-1
	jmpc fib_8
halt_loop:
	jmp halt_loop

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
